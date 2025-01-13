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

## Why



## Lib

`/lib/jdd-111.jar` is a [modified version](https://github.com/Augists/jdd) of [jdd library](https://bitbucket.org/vahidi/jdd) with variable `mkCount`.
Different order may cause `jdd` to create more BDD variables for these reason in [#Why](https://github.com/Augists/bdd-undergra-thinking#Why).