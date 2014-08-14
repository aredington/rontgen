# rontgen

Röntgen is sharp, dangerous, and highly opinionated java
interop. Röntgen can *peer* upon any java instance, returning an
immutable map that shows the state of all the fields of that
instance. Röntgen can also *bash* into any java instance an immutable
map of values, destructively modifying the instance in order to push
data in place.

Röntgen ignores private and protected modifiers for fields.

## Usage

```clojure
user> (let [date (java.util.Date. 0)] (rontgen/peer date))
{:fastTime 0, :cdate nil}
```

```clojure
user> (let [date (java.util.Date. 0)] (rontgen/bash date {:fastTime 6000}))
#inst "1970-01-01T00:00:06.000-00:00"
```

## License
The MIT License (MIT)

Copyright (c) 2014 Alex Redington & Daemian Mack

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
