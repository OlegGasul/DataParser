package com.testtask;

import com.testtask.model.ListItem;
import com.testtask.model.SortedList;

import java.io.*;
import java.util.Comparator;
import java.util.EnumMap;

public class Parser {
    private enum RowType {
        BID("bid"), ASK("ask"), SPREAD("spread");

        private final String name;

        RowType(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public static RowType findByName(String name){
            for (RowType rowType : values()) {
                if( rowType.name.equals(name)) {
                    return rowType;
                }
            }
            return null;
        }
    }

    private EnumMap<RowType, SortedList<ListItem>> data = new EnumMap<RowType, SortedList<ListItem>>(RowType.class);
    private Comparator<ListItem> priceComparator = new Comparator<ListItem>() {
        @Override
        public int compare(ListItem item1, ListItem item2) {
            return item2.price - item1.price;
        }
    };

    private SortedList<ListItem> getOrCreate(RowType rowType) {
        SortedList list = data.get(rowType);
        if (list == null) {
            list = new SortedList(priceComparator);
            data.put(rowType, list);
        }

        return list;
    }

    private boolean buy(int size) {
        SortedList<ListItem> list = getOrCreate(RowType.BID);
        if (list.size() == 0)
            return false;

        ListItem item = list.get(list.size() - 1);
        if (item.size < size)
            return false;

        item.size -= size;

        return true;
    }

    private boolean sell(int size) {
        SortedList<ListItem> list = getOrCreate(RowType.BID);
        if (list.size() == 0)
            return false;

        ListItem item = list.get(0);
        if (item.size < size)
            return false;

        item.size -= size;

        return true;
    }

    private int getSize(int price) {
        SortedList<ListItem> list = getOrCreate(RowType.BID);
        for (ListItem item : list) {
            if (item.price == price)
                return item.size;
        }

        list = getOrCreate(RowType.ASK);
        for (ListItem item : list) {
            if (item.price == price)
                return item.size;
        }

        list = getOrCreate(RowType.SPREAD);
        for (ListItem item : list) {
            if (item.price == price)
                return item.size;
        }

        return 0;
    }

    public Parser(File input, File output) {
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output), "utf-8"));

            String line;
            while ((line = br.readLine()) != null) {
                String[] lineData = line.split(",");
                switch (lineData[0]) {
                    case "u":
                        RowType rowType = RowType.findByName(lineData[3]);
                        SortedList list = getOrCreate(rowType);
                        list.add(new ListItem(Integer.parseInt(lineData[1]), Integer.parseInt(lineData[2])));

                        break;

                    case "o":
                        String operation = lineData[1];
                        if ("sell".equals(operation)) {
                            sell(Integer.parseInt(lineData[2]));
                        } else if ("buy".equals(operation)) {
                            buy(Integer.parseInt(lineData[2]));
                        } else {
                            throw new IllegalArgumentException("Operation " + operation + " not found.");
                        }

                        break;

                    case "q":
                        String query = lineData[1];
                        if ("best_bid".equals(query)) {
                            SortedList<ListItem> bids = getOrCreate(RowType.BID);
                            if (bids.size() > 0) {
                                ListItem bid = bids.get(0);
                                writer.println(bid.price + ", " + bid.size);
                            }
                        } else if ("best_ask".equals(query)) {
                            SortedList<ListItem> asks = getOrCreate(RowType.ASK);
                            if (asks.size() > 0) {
                                ListItem ask = asks.get(0);
                                writer.println(ask.price + ", " + ask.size);
                            }
                        } else if ("size".equals(query)) {
                            int size = getSize(Integer.parseInt(lineData[2]));
                            writer.println(size);
                        } else {
                            throw new IllegalArgumentException("Query " + query + " not found.");
                        }

                        break;
                }
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: input_file_path output_file_path");
            System.exit(1);
        }

        new Parser(new File(args[0]), new File(args[1]));
    }
}
