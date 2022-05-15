# Sequence Alignment

### Tate Osborne
### Spring 2022
---

This project is an algorithm for `sequence-alignment`. The algorithm finds the optimal alignment of two strings, based on various costs for matched letters.

__Costs__
- perfect match = `0.0`
  - [ex] `a-a` 
- consonant to consonant / vowel to vowel = `1.0`
  - [ex] `c-k` / `e-i`
- gap = `2.0`
  - [ex] `m-_`
- other = `3.0`
    - `o-h`

This algorithm is implemented with dynamic programming.

