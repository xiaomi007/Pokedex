# Pokédex

This demo app display a list of pokemon and a search field to find Pokemons which match the text entered.

The search will return any pokemon which match the input text.

The list of Pokémon is provided by [PokéApi](https://pokeapi.co).

### Libraries used

* [RxJava2](https://github.com/ReactiveX/RxJava)
* [Picasso](http://square.github.io/picasso/)
* [Retrofit](https://square.github.io/retrofit/)
* [Android Support](https://developer.android.com/topic/libraries/support-library/) (Recycler view, AppCompat)
* [Android Architecture](https://developer.android.com/topic/libraries/architecture/) (Room, Livedata, ViewModel and Paging)

### Screens
First page will load the list of pokemon from PokeApi
![start screen][screen0]

When the app starts, it will display a list of all pokemon
![full screen][screen1]

Search for Pik will show Pikachu and Pikipek
![search scree][screen2]

[screen0]: screen0.jpg
[screen1]: screen1.jpg
[screen2]: screen2.jpg
