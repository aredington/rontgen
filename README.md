# rontgen

Leiningen dependency (Clojars): `[rontgen "1.000000000001.0"]`.

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

```clojure
user> (rontgen/peer java.util.Calendar)
{:TUESDAY 3, :MONTH_MASK 4, :DAY_OF_WEEK_IN_MONTH_MASK 256, :cachedLocaleData {}, :SATURDAY 7, :JANUARY 0, :MINUTE 12, :DAY_OF_YEAR_MASK 64, :ERA 0, :MARCH 2, :COMPUTED 1, :DST_OFFSET_MASK 65536, :YEAR_MASK 2, :DATE 5, :FRIDAY 6, :AM_PM 9, :WEEK_OF_MONTH_MASK 16, :DAY_OF_WEEK 7, :DAY_OF_MONTH_MASK 32, :MINIMUM_USER_STAMP 2, :DAY_OF_WEEK_MASK 128, :MONDAY 2, :HOUR_MASK 1024, :PM 1, :$assertionsDisabled true, :WEEK_OF_YEAR 3, :HOUR_OF_DAY_MASK 2048, :ZONE_OFFSET 15, :DST_OFFSET 16, :JUNE 5, :currentSerialVersion 1, :DECEMBER 11, :MILLISECOND 14, :AUGUST 7, :MONTH 2, :ALL_FIELDS 131071, :NOVEMBER 10, :YEAR 1, :HOUR 10, :THURSDAY 5, :FIELD_NAME #<String[] [Ljava.lang.String;@24b0d6c7>, :DAY_OF_WEEK_IN_MONTH 8, :UNSET 0, :WEEK_OF_MONTH 4, :SEPTEMBER 8, :FIELD_COUNT 17, :MINUTE_MASK 4096, :SECOND 13, :WEEK_OF_YEAR_MASK 8, :SHORT 1, :serialVersionUID -1807547505821590642, :WEDNESDAY 4, :APRIL 3, :SUNDAY 1, :JULY 6, :HOUR_OF_DAY 11, :DAY_OF_MONTH 5, :LONG 2, :ALL_STYLES 0, :UNDECIMBER 12, :ZONE_OFFSET_MASK 32768, :FEBRUARY 1, :SECOND_MASK 8192, :ERA_MASK 1, :MILLISECOND_MASK 16384, :DATE_MASK 32, :OCTOBER 9, :MAY 4, :AM 0, :DAY_OF_YEAR 6, :AM_PM_MASK 512}
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
