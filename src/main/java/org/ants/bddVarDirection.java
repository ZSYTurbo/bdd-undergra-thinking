package org.ants;

import jdd.bdd.BDD;
import jdd.bdd.NodeTable;

import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;

public class bddVarDirection {

    static final int IP_LENGTH = 32;
    static final int IP_PART_LENGTH = 8;

    static final int BDD_NODE_TABLE_SIZE = 1000000;
    static final int BDD_CACHE_SIZE = 10000;

    private static BDD bddEngine = new BDD(BDD_NODE_TABLE_SIZE, BDD_CACHE_SIZE);;
    private static int[] bdds = new int[IP_LENGTH];
    private static int[] nbdds = new int[IP_LENGTH];

    private static ArrayList<String> IPs = new ArrayList<>();

    private static boolean IPnotValid(String[] ipSet) {
        return false;
    }

    /**
     * 31 (16 + 8 + 4 + 2 + 1) -> 0001 1111 -> [0, 0, 0, 1, 1, 1, 1, 1]
     * @param i 192 168 31 1
     * @param bitset append 8 bit
     * @param part offset in bitset
     */
    private static void int2BitSet(int i, BitSet bitset, int part) {
        int offset = (part + 1) * IP_PART_LENGTH - 1;
        for (int j = 0; j < IP_PART_LENGTH; j++) {
            if ((i & 1) == 1) {
                bitset.set(offset - j);
            }
            i >>= 1;
        }
    }

    /**
     * Convert from ip string to a binary bitset
     * @param ip String 192.168.31.1
     * @return BitSet
     */
    private static BitSet IP2BitSet(String ip) {
        String[] ipSet = ip.split("\\.");

        if (IPnotValid(ipSet)) {
            return null;
        }

        BitSet bitSet = new BitSet(IP_LENGTH);

        for (int i = 0; i < ipSet.length; i++) {
            // convert string to int
            int partedIP = Integer.parseInt(ipSet[i]);
            int2BitSet(partedIP, bitSet, i);
        }

        return bitSet;
    }

    /**
     * Test for the direction when creating BDD variables
     *  - positive order [x0, x1, x2, x3]
     *  - reverse order  [x3, x2, x1, x0]
     * @param reverse false(default) / true
     */
    private static void createVarWithDirection(boolean reverse) {
        for (int i = 0; i < IP_LENGTH; i++) {
            int idx = i;
            // TODO: problem 1
//            if (!reverse) {
            if (reverse) { // default
                idx = IP_LENGTH - i - 1;
            }
            bdds[idx] = bddEngine.createVar();
            nbdds[idx] = bddEngine.not(bdds[idx]);
        }
    }

    /**
     * construct ip bdd from bitset
     * create order - construct order
     *  - pos-rev  x3 ^ -x2 ^ -x1 ^ -x0
     *  - rev-rev  x0 ^ -x1 ^ -x2 ^ -x3
     *  - pos-pos -x0 ^ -x1 ^ -x2 ^ x3
     *  - rev-pos -x3 ^ -x2 ^ -x1 ^ x0
     * @param ip ip address binary bitset
     * @param reverse true(default) / false
     * @return constructed ip bdd
     */
    private static int constructBDDWithDirection(BitSet ip, boolean reverse) {
        int ipBDD = 1;

        for (int i = IP_LENGTH - 1; i >= 0; i--) {
            int idx = i;
            // TODO: problem 2
//            if (reverse) {
            if (!reverse) { // default
                idx = IP_LENGTH - i - 1;
            }
            int ipBit = ip.get(idx) ? bdds[idx] : nbdds[idx];
            ipBDD = bddEngine.and(ipBDD, ipBit);
        }

        return ipBDD;
    }

    /**
     * IP constructor wrapper
     * @param ip ip address string with decimal separator
     * @param problemNumber
     *  - 1: pos-rev or rev-rev
     *  - 2: pos-rev or pos-pos
     * @return constructed ip bdd
     */
    private static int constructIP(String ip, int problemNumber) {
        BitSet ipBitSet = IP2BitSet(ip);

        boolean flag = problemNumber == 1;
        createVarWithDirection(flag);

        return constructBDDWithDirection(ipBitSet, flag);
    }

    /**
     * Read file content from path
     *
     * @param fileInPath
     * @throws IOException
     */
    public static void getFileContent(Object fileInPath) throws IOException {
        BufferedReader br = null;
        if (fileInPath == null) {
            return;
        }
        if (fileInPath instanceof String) {
            br = new BufferedReader(new FileReader(new File((String) fileInPath)));
        } else if (fileInPath instanceof InputStream) {
            br = new BufferedReader(new InputStreamReader((InputStream) fileInPath));
        }
        String line;
        while ((line = br.readLine()) != null) {
            IPs.add(line);
//            System.out.println(line);
        }
        br.close();
    }

    /**
     * read ip addresses from src/main/resources/ips
     */
    private static void readIPs() {
        String filename = "ips";
        String path = bddVarDirection.class.getResource("").getPath();
        String filePath = path + filename;

        try {
            getFileContent(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * benchmark
     * multi ip address with different length
     */
    public static void main(String[] args) {

        // TODO: choose your problem number
        final int PROBLEM_NUMBER = 1;
//        final int PROBLEM_NUMBER = 2;

        readIPs();

        long startTime = System.nanoTime();

        int ipGroup = 0;
        for (String ip : IPs) {
            int ipBDD = constructIP(ip, PROBLEM_NUMBER);
            ipGroup = bddEngine.or(ipBDD, ipGroup);
        }

        long endTime = System.nanoTime();

        // TODO: add your benchmark
        System.out.println("total run time: " + (endTime - startTime));
        System.out.println("bdd node mk count: " + NodeTable.mkCount);
    }
}
