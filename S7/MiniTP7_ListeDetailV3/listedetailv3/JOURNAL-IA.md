- Ce test vérifie LE bon point : il insère un prix null et assert l'ordre exact des ids
  (8000, 5000, 3000 puis le null en dernier), on est très loin du « constate que ça ne
  plante pas », c'est exactement l'énigme du litchi.
- Mais il ne tourne pas tel quel : il appelle dao.insertAll() alors que notre DAO s'appelle
  insererTous, et il suppose un échafaudage de tests (AndroidJUnit4, Room in-memory,
  coroutines-test) absent du projet — sans parler de allowMainThreadQueries, toléré en
  test seulement.
- Bilan : je garde l'idée (asserter l'ordre, pas l'absence de crash), pas le fichier —
  dans ce TP, le compilateur Room + l'observation du litchi en bas de liste SONT le
  premier test, l'harnais Robolectric est un sujet d'après-module.