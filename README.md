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

## Result-Problem2

The time of constructing IPs from number of 0 to 1000  forward and reverse

<img title="" src="file:///C:/Users/Primo/AppData/Roaming/marktext/images/2025-01-18-23-38-10-image.png" alt="" width="315"><img title="" src="file:///C:/Users/Primo/AppData/Roaming/marktext/images/2025-01-18-23-38-25-image.png" alt="" width="316">

## Why

#### #1 mkcout

The node make count increase almost linearly, and the reverse case has lower count. The reason is related to the  inner order of operation "and". (For both reverse and forward, the final bdd of a single IP is the same due to we have same createvar order, so the "or" opreation of different bdd for both situation should be the same, so we can just talk about "and")

The "and" operation begins with the roots of two bdds, if the vars of two roots are different, it recursively goes deeper of the one who has lower var. For the reverse case, each time the new node to be added to the bdd always has the  lowest var, so it go deeper and dirtctly get to terminal 1 and 0, then we can directly put the bdd below the new var regardless of what's exacly in the bdd. Unfortunately when in forward case, the new node always has the highest var, so each time adding a new node, the "and" operation has to recursive go through the whole bdd till it get to the end, add the node, and then recursive go back to the root. so in the forward order, it watse a lot of time going through the bdd, which is  unnecessary because we know each time the new node( or bdd) is just a node directly attach to the terminal.

I didn't mention operation cache above, but the influence of it to the difference is ignorable if the IPs are random.   The two situation have their own order and the corresponding cache, each one has better performance depends on the pattern of IP data.

#### #2 total time



## Lib

`/lib/jdd-111.jar` is a [modified version](https://github.com/Augists/jdd) of [jdd library](https://bitbucket.org/vahidi/jdd) with variable `mkCount`.
Different order may cause `jdd` to create more BDD variables for these reason in [#Why](https://github.com/Augists/bdd-undergra-thinking#Why).