# Rakennekuvaus

Ohjelman rakenne on yksinkertainen. On Peli, jossa on Board, joka koostuu Bubble-olioista. Pelillä on renderer, missä on BoardRenderer, mikä koostuu Bubble Renderereistä. Eli logiikka ja piirtäminen on erotettu toisistaan täysin.

Pelitiloja hallitsee Slick2D-kirjasto ja CrazyGame-tila hoitaa pelilogiikan ja renderöijien päivittämisen.
