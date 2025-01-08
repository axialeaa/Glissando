Happy new year! Here's a small but certainly not trivial update focusing on some back-end changes improving inter-mod compatibility. Also, I have finally written the wiki page on creating your own note block instruments with **Glissando**'s system. Go check it out [here][instrument-page]!

## ➕ Additions
Added a new option under the Visuals page: Solmization! For those unaware (as I was before this week!) "solmization" is defined by [Oxford Language][oxlang] as:

> ### SOLMIZATION
> ###### /ˌsɒlmɪˈzeɪʃ(ə)n/
> <code><i>noun</i></code>
> a system of associating each note of a scale with a particular syllable, especially to teach singing.

In essence, enabling this option turns your normal _C, D, E, F, G, A, B_ scale into _Do, Re, Mi, Fa, Sol, La, Ti_ (at least until **Glissando** gets translated into other languages)! Interestingly, English-speaking countries almost exclusively use the "solfège" sequence, while the Chinese Han's People use _六, 五, 乙, 上, 尺, 工, 凡_ and Japan uses _Ha, Ni, Ho, He, To, I, Ro_. <sup>[[source]][solmization]</sup>

_Language is so cool..._

## 🔧 Changes
Note block instrument block tags now include some vanilla and fabric convention tags inside them. This means that many blocks from other mods should work right away, as long as they have been added to those tags in the first place!

> [!NOTE]
> I am looking for feedback on this. You should expect slight deviances from vanilla Minecraft behavior but if you find any tags that haven't been added, pop me a bug report on [GitHub][github-issues]. Thanks!

In other news, the `Visuals > Keyboard Color Predicate` has been refactored to Keyboard Color <u>Mode</u>. You will need to re-enter the selection you previously saved, if you did.

More on visuals: accidental notes now display in tooltips as F♯₃/G♭₃ for example, instead of just F♯₃. The way the subscript octave number is shown has also changed to better accommodate custom instruments with a different `octave` number.

## 🐛 Fixes
- #1: The game should no longer crash with **Glissando** installed in some circumstances (but like actually this time)

[instrument-page]: https://github.com/axialeaa/Glissando/wiki/Data%E2%80%90Driven-Instruments
[oxlang]: https://languages.oup.com/google-dictionary-en/
[solmization]: https://en.wikipedia.org/wiki/Solmization
[github-issues]: https://github.com/axialeaa/Glissando/issues