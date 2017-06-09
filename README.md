# nanook

[![Build Status](https://travis-ci.org/felipeanchieta/nanook.svg?branch=master)](https://travis-ci.org/felipeanchieta/nanook)

Nanook is a Clojure webserver designed to accomplish Nubank interview requirements.

More than that, Nanook is also a checking account system, in which you can operate credit and debit within a given account, retrieve its balance, and its statement. Ultimately, the bank can retrieve statistics regarding periods of debt of a given account in order to explore this to increase its profits.

## Usage

### Running Nanook

Nanook can be run either by using Leiningen with the command "lein ring server", under the Leiningen Ring plugin or by Docker (yep, there's a Dockerfile right there and it semms to work).

The tests can be run with "lein test". The HTTP request are mocked out with Ring Mock, so don't bother plugging your network cable if you didn't do so yet. Unless you don't have installed Leiningen, then you have to have Internet access in order to download the plugins. 

By the way: you can also check the code coverage with "lein cloverage".

### API usage

The API is largely (100%) based on GET/POST requests with a JSON body. Please, keep you JSON body valid, Nanook usually doesn't deal well with malformed JSONs.

- Credit

First of all, as good person you can credit some money in the account like this:

```
POST /accounts/<account_code>/credit

{
   "amount" : float,
   "description" : string, min 4
   "timestamp" : string optional ISO 8601
}
```

The response will be like

```
{
   "operation": "credit",
   "account": "12345",
   "amount": 1000,
   "timestamp": "2017-06-09T09:58:17.553-03:00",
   "description": "Salary",
   "uuid": "3a48ab33-b5a9-4798-92fa-fc1b2d03765f"
}
```

- Debit

When you make a purchase or any other kind of debit, you can do like this:

```
POST /accounts/<account_code>/debit

{
   "amount" : float,
   "description" : string, min 4
   "timestamp" : string optional ISO 8601
}
```

The response will be like

```
{
   "operation": "debit",
   "account": "12345",
   "amount": 567.50,
   "timestamp": "2017-06-09T09:58:17.553-03:00",
   "description": "Purchase",
   "uuid": "3a48ab33-b5a8-4798-92fa-fc1b2d03765f"
}
```

- Balance

When in doubt about making a purchase or not, you can retrieve the balance like this:

GET /accounts/<account_code>/balance

The response can be like this:

```
{
   "balance" : 100.58
}
```

- Statement

When you have even more doubts, you can ask a statement as well:

```
/account/<account_code>/statement

{
    "from" : date string dd/mm/YYYY,
    "to" : date string dd/mm/YYYY
}
```

```
{
   "statement":￼[
￼     {
         "date": "09/06/2017",
         "description": "aaaaaa",
         "amount": 1000
      }
   ]
}
```

- Periods of debt

When you want to profit, here's the deal: extract the periods of debt of an account:

POST /account/<account_code>/debt-periods

```
{
    "from" : date string dd/mm/YYYY,
    "to": date string dd/mm/YYYY,
}
```

You can check on test/nanook some examples on this one.

## Cloverage status

```
|-------------------+---------+---------|
|         Namespace | % Forms | % Lines |
|-------------------+---------+---------|
|       nanook.core |  100,00 |  100,00 |
|     nanook.facade |   76,97 |  100,00 |
|    nanook.handler |   94,96 |   96,15 |
|      nanook.utils |  100,00 |  100,00 |
| nanook.validation |  100,00 |  100,00 |
|-------------------+---------+---------|
|         ALL FILES |   85,66 |   98,70 |
|-------------------+---------+---------|

```

It's bizarre not to think that all forms are being covered in nanook.facade thought 100% of lines are actually being covered, so I believe this must a bug or some mistake of mine.

## Lints statuses

development@osp06083d:~/Documents/kendrick/nanook$ lein eastwood
== Eastwood 0.2.4 Clojure 1.8.0 JVM 1.8.0_131
Directories scanned for source files:
  src test
== Linting nanook.utils ==
== Linting nanook.core ==
== Linting nanook.facade ==
== Linting nanook.validation ==
== Linting nanook.handler ==
2017-06-09 09:43:57.819:INFO::main: Logging initialized @6526ms
== Linting nanook.basicoperations-test ==
== Linting nanook.statements-test ==
== Linting nanook.balance-test ==
== Linting nanook.debt-periods-test ==
== Warnings: 0 (not including reflection warnings)  Exceptions thrown: 0
development@osp06083d:~/Documents/kendrick/nanook$ lein cljfmt check
All source files formatted correctly

## License

Copyright © 2017 Felipe Anchieta Santos Costa

Distributed under the Eclipse Public License 1.0
