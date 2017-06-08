# nanook

[![Build Status](https://travis-ci.org/felipeanchieta/nanook.svg?branch=master)](https://travis-ci.org/felipeanchieta/nanook)

Nanook is a Clojure webserver designed to accomplish Nubank interview requirements.

More than that, Nanook is also a checking account system, in which you can operate credit and debit within a given account, retrieve its balance, and its statement. Ultimately, the bank can retrieve statistics regarding periods of debt of a given account in order to explore this to increase its own profits.

## Usage

### Running Nanook

Nanook can be run either by using Leiningen with the command "lein ring server", under the Leiningen Ring plugin or by Docker (yep, there's a Dockerfile right there and it works).

The automated tests can be run with "lein test", using Leiningen again. The HTTP request are mocked out with Ring Mock, so don't bother plugging your network cable if you didn't do so yet. Unless you don't have installed Leiningen, then you have to have Internet access in order to download the plugins. By the way: you can also check the code coverage with "lein cloverage".

### API usage

The API is largely (100%) based on GET/POST requests with a JSON body. Please, keep you JSON body valid, Nanook usually doesn't deal well with malformed JSONs.

- Credit

First of all, as good person you can credit some money in the account like this:

```
POST /accounts/<account_code>/credit

{
   "amount" : float,
   "description" : string, min 4
   "tpimestamp" : string optional
}

- Debit

When you make a purchase or any other kind of debit, you can do like this:

POST /accounts/<account_code>/debit

{
   "amount" : float,
   "description" : string, min 4
   "timestamp" : string optional
}

- Balance

When in doubt about making a purchase or not, you can retrieve the balance like this:

GET /accounts/<account_code>/balance

- Statement

When you have even more doubts, you can ask a statement as well:

/account/<account_code>/statement

{
    "from" : date,
    "to" : date
}

- Periods of debt

When you want to profit, here's the deal: extract the periods of debt of an account:

POST /account/<account_code>/debt_periods

{
    "from" : date,
    "to": date
}
```


## License

Copyright © 2017 Felipe Anchieta Santos Costa

Distributed under the Eclipse Public License 1.0
