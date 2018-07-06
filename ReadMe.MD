==== PopularMovies ====
This is my implementation of the Popular Movies project for the Udacity Android Developers nanodegree.

===== How to build =====
First and foremost, you will need to obtain an API key from the TMDB API: https://www.themoviedb.org
Following that, you will need to under values create an api_keys.xml as a String XML file.
Below is most of the source from my implementation, save the API Key.

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="tmdb">Your TMDB API Key goes here</string>
</resources>
```

Following that, you should be able to build using Gradle and it should function.
