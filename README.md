# clj-abcnotation

A Clojure library for parsing music files in ABC Notation.

The output of parsing an ABC Notation string or file is a Clojure map that can then
be used, in my case for generating sounds with Overtone.

## Usage

The basic usages is:

```clojure
(use 'clj-abcnotation.core)

(parse abc-string)
```

## License

Copyright © 2014 John Kane

Distributed under the Eclipse Public License, the same as Clojure.
