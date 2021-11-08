# Progressive
[![Build Status](https://app.travis-ci.com/CrissNamon/progressive.svg?branch=main)](https://app.travis-ci.com/CrissNamon/progressive) ![release](https://img.shields.io/github/v/release/crissnamon/progressive?include_prereleases)
<p>Progressive is a simple game framework, which provides you predefined way to develop your game logic.</p>

#### About project
___
<p>
Progressive gives you an IoC/DI container with auto injections feature through annotations, game objects, game scripts and much more! It's is like a constructor. You can add game objects and attach game scripts to them.

Main features:
- IoC/DI container with auto injections through annotations, frame updates
- GameObjects and GameScripts
- State manager
- The global observer for components connection
- Proxy classes and proxy objects creation through annotations with custom method interceptors
</p> 

#### Resources
___
* Learn more at Progressive [Wiki](https://github.com/CrissNamon/progressive/wiki)
* Look at some examples in [Main](https://github.com/CrissNamon/progressive/blob/main/src/main/java/ru/danilarassokhin/main/Main.java)
* See javadoc [here](https://crissnamon.github.io/progressive/)

#### Dependencies and source 
___

[BasicProxyCreator](https://github.com/CrissNamon/progressive/blob/main/src/main/java/ru/danilarassokhin/progressive/basic/BasicProxyCreator.java) uses [ByteBuddy](https://bytebuddy.net/) for proxy creation. You need to add byte-buddy lib to be able to use proxy classes in your project. For android development you also need to add [byte-buddy-android](https://github.com/raphw/byte-buddy/tree/master/byte-buddy-android) lib.
<p>All other parts of Progressive have no dependencies and use only Java 8.</p> 

#### Repository info
___
* The main branch contains stable release
* Development branch contains WIP code
* Progressive is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0)

#### Authors
___
* [Danila Rassokhin](https://gihub.com/crissnamon) [![Twitter](https://img.shields.io/twitter/follow/kpekepsalt?style=social)](https://twitter.com/kpekepsalt)