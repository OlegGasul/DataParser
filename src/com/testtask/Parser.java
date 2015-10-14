package com.testtask;

import com.testtask.model.SortedList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    }

    private EnumMap<RowType, SortedList> data = new EnumMap<RowType, SortedList>(RowType.class);

    private SortedList createOrGet(RowType rowType) {
        SortedList list = data.get(rowType);
        if (list == null) {
            list = new SortedList();
            data.put(rowType, list);
        }

        return list;
    }

    public Parser(File input, File output) {
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineData = line.split(",");
                switch (lineData[0]) {
                    case "u":
                        RowType rowType = Enum.valueOf(RowType.class, lineData[3]);
                        SortedList list = createOrGet(rowType);
                        break;

                    case "o":

                        break;

                    case "q":

                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: ...");
            System.exit(1);
        }


    }
}
