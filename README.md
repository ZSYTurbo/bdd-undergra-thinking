# BDD Variable Order Thinking

## Problem Set

1. **create** variable order
   - `createVar` and `ithVar`
2. **construct** variable order
   - `apply` operation

## Start

Open the project in IDEA. (`/.idea` is preserved for plugin `google-java-format` and other settings)

Generate dataset by

```shell
python3 utils/randomCreateIP.py
```

which is a random IP address generator and will store all IPs in `src/main/resources/org/ants/ips`

Then run the main code in java.

## Result

The mkcount of constructing 1000 IPs  forward and reverse. Total run time is roughly related to mkcount but the specific value is unstable, so I just list mkcount here.

|                   | create-rev | create-for |
|:-----------------:| ---------- | ---------- |
| **construct-rev** | 1111699    | 26146      |
| **construct-for** | 1005654    | 121957     |

## Why

# 

#### #2 Construct

When create variables in the same order, constructing the bdd in the opposite order of creating brings less mkcount. The reason is related to the  inner order of operation "and". (For both reverse and forward, the final bdd of a single IP is the same due to we have same createvar order, so the "or" opreation of different bdd for both situation should be the same, so we can just talk about "and")

The "and" operation begins with the roots of two bdds, if the vars of two roots are different, it recursively goes deeper of the one who has lower var. For the reverse case, each time the new node to be added to the bdd always has the  lowest var, so it go deeper and dirtctly get to terminal 1 and 0, then we can directly put the bdd below the new var regardless of what's exacly in the bdd. Unfortunately when in forward case, the new node always has the highest var, so each time adding a new node, the "and" operation has to recursive go through the whole bdd till it get to the end, add the node, and then recursive go back to the root. so in the forward order, it watse a lot of time going through the bdd, which is  unnecessary because we know each time the new node( or bdd) is just a node directly attach to the terminal.

I didn't mention operation cache above, but the influence of it to the difference is ignorable if the IPs are random.   The two situation have their own order and the corresponding cache, each one has better performance depends on the pattern of IP data.

## Lib

`/lib/jdd-111.jar` is a [modified version](https://github.com/Augists/jdd) of [jdd library](https://bitbucket.org/vahidi/jdd) with variable `mkCount`.
Different order may cause `jdd` to create more BDD variables for these reason in [#Why](https://github.com/Augists/bdd-undergra-thinking#Why).