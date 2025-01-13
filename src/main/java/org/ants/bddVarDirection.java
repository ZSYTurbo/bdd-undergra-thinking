package org.ants;

import jdd.bdd.BDD;
import jdd.examples.Simple2;

import java.util.BitSet;

public class createVarDirection {

    static final int IP_LENGTH = 32;
    static final int IP_PART_LENGTH = 8;

    static final int BDD_NODE_TABLE_SIZE = 10000;
    static final int BDD_CACHE_SIZE = 1000;

    private static BDD bddEngine;
    private static int[] bdds = new int[IP_LENGTH];
    private static int[] nbdds = new int[IP_LENGTH];

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
//            bitSet.set(i);
        }

        return bitSet;
    }

    /**
     * Test for the direction when creating BDD variables
     * 正 [x0, x1, x2, x3] / 反 [x3, x2, x1, x0]
     * @param reverse True if from right to left
     */
    private static void createVarWithDirection(boolean reverse) {
        for (int i = 0; i < IP_LENGTH; i++) {
            int idx = i;
            if (reverse) {
                idx = IP_LENGTH - i - 1;
            }
            bdds[idx] = bddEngine.createVar();
            nbdds[idx] = bddEngine.not(bdds[idx]);
        }
    }

    /**
     * construct ip bdd from bitset
     * @param ip ip address binary bitset
     * @param bdds initialized bdd list
     * @return constructed ip bdd
     */
    private static int constructBDD(BitSet ip, int[] bdds) {
        int ipBDD = 1;

        for (int i = IP_LENGTH - 1; i >= 0; i--) {
            int ipBit = ip.get(i) ? bdds[i] : nbdds[i];
            ipBDD = bddEngine.and(ipBDD, ipBit); // 正反 x3 ^ -x2 ^ -x1 ^ -x0 / 反反 x0 ^ -x1 ^ -x2 ^ -x3 / 正正 -x0 ^ -x1 ^ -x2 ^ x3 / 反正 -x3 ^ -x2 ^ -x1 ^ x0
        }

        return ipBDD;
    }

    // 多个ip or, ip长度不同
    // 12 T1, 13 T2
    public static void main(String[] args) {
        String testIP = "192.168.31.1"; // 0.0.0.1
        BitSet ipSet = IP2BitSet(testIP); // {3}

        bddEngine = new BDD(BDD_NODE_TABLE_SIZE, BDD_CACHE_SIZE);
        createVarWithDirection(true);;

        int ip = constructBDD(ipSet, bdds);
        System.out.println(bddEngine.satCount(ip));
    }
}
