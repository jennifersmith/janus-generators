

After a day spent in the Royal Festival Hall, sweating in the sunshine but more importantly hanging out with some very smart members of the London Clojure Community, I finally finished what I tried to do for the last couple of months.

(re-generate "[A-Z]+" 20) => ("A" "B" "C" "D" "E" "F" "G" "AA" "H" "I" "J" "K" "AB" "L" "M" "N" "O" "BA" "AC" "P")

It's kind of neat but with a massive limitation that I will cover at the end of this post.


## Step 1 - Parse the regex

This took a lot of the early days (see my early notes in the Github repo!) but I finally settled on using [instaparse](https://github.com/Engelberg/instaparse) to parse a regex. Instaparse is pretty neat and feels very clojure-appropriate. 