package phonebook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    static long linearsortingTime = 0;
    static long bubblesortingTime = 0;
    static long jumpsearchTime = 0;

    static long quicksortTime = 0;

    static long binarysearchTime = 0;

    static long hashtableCreation = 0;

    static long hashtableSearch = 0;

    public static void main(String[] args) throws IOException, InterruptedException {
        SimpleDateFormat formatter = new SimpleDateFormat("mm-ss-SSS");


        HashSet<String> findList = readFinds();
        hashtableCreation = System.currentTimeMillis();
        HashSet<String>  directoryList= readDirectory();
        hashtableCreation = System.currentTimeMillis() - hashtableCreation;

        linearsortingTime = System.currentTimeMillis();

        System.out.println("Start searching (linear search)...");

        hashtableSearch = System.currentTimeMillis();
        int maxFound = linearSearch(findList, directoryList);
        hashtableSearch = System.currentTimeMillis() - hashtableSearch;

        linearsortingTime = System.currentTimeMillis() - linearsortingTime;
        List<String> time = List.of(formatter.format(linearsortingTime).split("-"));
        String timeFormatted = "Time taken: " + time.get(0)+ " min. " + time.get(1) + " sec. " + time.get(2) + " ms.";
        System.out.println( new StringBuilder(String.format("Found " + maxFound + " / " + findList.size() + " entries. " + timeFormatted)));


        System.out.println("\nStarting searching (bubble sort + jump search)...");

        List<String> sortedDirectoryList  =  new ArrayList<>(directoryList);

        List<String> unSortedFindList  =  new ArrayList<>(findList);

        bubblesortingTime = System.currentTimeMillis();

//        boolean finished = bubbleSort(sortedDirectoryList);

        Collections.sort(sortedDirectoryList, String.CASE_INSENSITIVE_ORDER);
        bubblesortingTime = System.currentTimeMillis() - bubblesortingTime;

        boolean finished = bubblesortingTime < linearsortingTime * sortedDirectoryList.size();

        if (finished) {
            maxFound = 0;
            long startSearching = System.currentTimeMillis();
            for (String name: unSortedFindList) {
                if(jumpSearch(sortedDirectoryList, name)  > -1) {
                    maxFound++;
                }
            }

            jumpsearchTime = System.currentTimeMillis() - startSearching;

        } else {
            jumpsearchTime = linearsortingTime;
        }

        time = List.of(formatter.format(bubblesortingTime + jumpsearchTime).split("-"));
        timeFormatted = "Time taken: " + time.get(0)+ " min. " + time.get(1) + " sec. " + time.get(2) + " ms.";
        System.out.println( new StringBuilder(String.format("Found " + maxFound + " / " + findList.size() + " entries. " + timeFormatted)));


        List<String> sortingTime = List.of(formatter.format(bubblesortingTime).split("-"));

        String sortingTimeFormatted;
        if (finished) {
            sortingTimeFormatted = "Sorting time: " + sortingTime.get(0) + " min. " + sortingTime.get(1) + " sec. " + sortingTime.get(2) + " ms.";
        } else {
            sortingTimeFormatted = "Sorting time: " + sortingTime.get(0) + " min. " + sortingTime.get(1) + " sec. " + sortingTime.get(2) + " ms." +
                    " - STOPPED, moved to linear search";
        }
        System.out.println(sortingTimeFormatted);

        sortingTime = List.of(formatter.format(jumpsearchTime).split("-"));
        String searchingTimeFormatted = "Searching time: " + sortingTime.get(0) + " min. " + sortingTime.get(1) + " sec. " + sortingTime.get(2) + " ms.";
        System.out.println(searchingTimeFormatted);

        quicksortTime = System.currentTimeMillis();

        System.out.println("Starting searching (quick sort + binary search)...");

        sortedDirectoryList  =  quickSort(new ArrayList<>(directoryList));

        Thread.sleep(500);
        quicksortTime = System.currentTimeMillis() - quicksortTime;

        binarysearchTime = System.currentTimeMillis();

        maxFound = 0;
        for(String name: findList) {
            if (binearySearch(sortedDirectoryList, name, 0, sortedDirectoryList.size()-1)) {
                maxFound++;
            }
        }

        binarysearchTime = System.currentTimeMillis() - binarysearchTime;


        time = List.of(formatter.format(quicksortTime + binarysearchTime).split("-"));
        timeFormatted = "Time taken: " + time.get(0)+ " min. " + time.get(1) + " sec. " + time.get(2) + " ms.";
        System.out.println( new StringBuilder(String.format("Found " + maxFound + " / " + findList.size() + " entries. " + timeFormatted)));

        sortingTime = List.of(formatter.format(quicksortTime).split("-"));
        sortingTimeFormatted = "Sorting time: " + sortingTime.get(0) + " min. " + sortingTime.get(1) + " sec. " + sortingTime.get(2) + " ms.";
        System.out.println(sortingTimeFormatted);

        sortingTime = List.of(formatter.format(binarysearchTime).split("-"));
        searchingTimeFormatted = "Searching time: " + sortingTime.get(0) + " min. " + sortingTime.get(1) + " sec. " + sortingTime.get(2) + " ms.";
        System.out.println(searchingTimeFormatted);


        System.out.println("Start searching (hash table)...");

        maxFound = linearSearch(findList, directoryList);

        time = List.of(formatter.format(linearsortingTime + hashtableCreation).split("-"));
        timeFormatted = "Time taken: " + time.get(0)+ " min. " + time.get(1) + " sec. " + time.get(2) + " ms.";
        System.out.println( new StringBuilder(String.format("Found " + maxFound + " / " + findList.size() + " entries. " + timeFormatted)));


        sortingTime = List.of(formatter.format(hashtableCreation).split("-"));
        searchingTimeFormatted = "Creating time: " + sortingTime.get(0) + " min. " + sortingTime.get(1) + " sec. " + sortingTime.get(2) + " ms.";
        System.out.println(searchingTimeFormatted);

        sortingTime = List.of(formatter.format(hashtableSearch).split("-"));
        searchingTimeFormatted = "Searching time: " + sortingTime.get(0) + " min. " + sortingTime.get(1) + " sec. " + sortingTime.get(2) + " ms.";
        System.out.println(searchingTimeFormatted);

    }

    public static boolean binearySearch(List<String> directory, String name, int left, int right) {
        if (right >= left) {

            int mid = left + (right - left)/2;

            if (directory.get(mid).equals(name)) {
                return true;
            }

            if (directory.get(mid).compareTo(name) > 0) {
                return binearySearch(directory, name, left, mid-1);
            }

            return binearySearch(directory,name, mid+1, right);
        }
        return false;
    }


    public static List<String> quickSort(List<String> list) {
        if (list.isEmpty())
            return list; // start with recursion base case
        List<String> sorted;  // this shall be the sorted list to return, no needd to initialise
        List<String> smaller = new ArrayList<String>(); // Vehicles smaller than pivot
        List<String> greater = new ArrayList<String>(); // Vehicles greater than pivot
        String pivot = list.get(0);  // first Vehicle in list, used as pivot
        int i;
        String j;     // Variable used for Vehicles in the loop
        for (i=1;i<list.size();i++)
        {
            j=list.get(i);
            if (j.compareTo(pivot)<0)   // make sure Vehicle has proper compareTo method
                smaller.add(j);
            else
                greater.add(j);
        }
        smaller=quickSort(smaller);  // capitalise 's'
        greater=quickSort(greater);  // sort both halfs recursively
        smaller.add(pivot);          // add initial pivot to the end of the (now sorted) smaller Vehicles
        smaller.addAll(greater);     // add the (now sorted) greater Vehicles to the smaller ones (now smaller is essentially your sorted list)
        sorted = smaller;            // assign it to sorted; one could just as well do: return smaller

        return sorted;

    }


    public static HashSet<String> readDirectory() throws IOException {
        HashSet<String> resultList = new HashSet<>();

        String result = null;
        try (BufferedReader content = new BufferedReader(new FileReader("D:\\Java-course\\untitled\\directory.txt"))) {
            while(((result = content.readLine()) != null)) {
                List<String> results = List.of(result.split(" ",2));
                resultList.add(results.get(1));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }


    public static HashSet<String> readFinds() throws IOException {
        HashSet<String> resultList = new HashSet<>();

        String result = null;
        try (BufferedReader content = new BufferedReader(new FileReader("D:\\Java-course\\untitled\\find.txt"))) {
            while(((result = content.readLine()) != null)) {
                resultList.add(result);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    public static int linearSearch(HashSet<String> names, HashSet<String> directory) {
//        long start = System.currentTimeMillis();

        int maxFound = 0;
        for (String name: names) {
            if (directory.contains(name)) {
                maxFound++;
            }
        }

//        linearsortingTime = System.currentTimeMillis() - start;
        return maxFound;

    }


    public static boolean bubbleSort(List<String> list) {
        long startSorting = System.currentTimeMillis();

        for (int i = 0; i < list.size()-1 ; i++) {
            for (int j = 0; j < list.size()-i-1; j++) {
                long endSorting = System.currentTimeMillis();

                if (endSorting -  startSorting >= linearsortingTime * list.size()) {
                    return false;
                }
                String nameOne = list.get(j).toLowerCase(Locale.ROOT);
                String nameTwo = list.get(j+1).toLowerCase();
                if(nameOne.compareTo(nameTwo) > 0) {
                    Collections.swap(list, j,j+1);
                }

            }
        }
        return true;
    }

    public static int jumpSearch(List<String> list, String name) {
        int len = list.size();

        int step = (int) Math.floor(Math.sqrt(len));


        int prev = 0;

        while (list.get(Math.min(step, len) - 1).toLowerCase().compareTo(name.toLowerCase()) < 0) {
            prev = step;
            step += (int) Math.floor(Math.sqrt(len));
        }

        while (list.get(prev).toLowerCase(Locale.ROOT).compareTo(name.toLowerCase()) < 0) {
            prev++;

            if (prev == Math.min(step, len)) {
                return -1;
            }
        }

        if (list.get(prev).toLowerCase(Locale.ROOT).equals(name.toLowerCase())) {
            return prev;
        }
        return -1;
    }
}
