# WebDataReader
<<<<<<< HEAD
 A WebDataReader for several things
 
Usage:
1. Öffne die Class org.ms.core.reader.HttpReaderTest
2. In 'public static final String URL..' die URL erfassen, resp. anpassen. Beispiele sind vorgegeben.
3. Bei Bedarf weitere URL erfassen
4. Die erfassten URL auch weiter unten in der Methode 'public void testReadAddresses()' erfassen. Beispiele sind vorgegeben.
5. Für Ausführung in Codenvy: Über das Menu [Build] das Menuitem [Build] starten.
6. In der Console unten die Adressen rauskopieren.
7. Fertig!

Errors:
- Parserfehler in der Adresse
  Folgende Fehler werden bisher in den Spalten ausgegeben:
  - "NOT_CLEARLY_DEFINED_ADDRESS" : Die Felder "Street", "Number", "ZIP" und "City" sind nicht eindeutig und müssen manuell bereinigt werden.
  - "NOT_CLEARLY_DEFINED_ZIP_CITY" : Die Felder "ZIP" und "City" sind nicht eindeutig und müssen manuell bereinigt werden.

Nicht unterstützte Fälle:
1.  Einträge mit 6 statt der üblichen 5 Einträge. Beispiel "Gartencenter Raschle" unter http://yellow.local.ch/de/print/q?page=9&print=text&rid=Iqus&what=gartenbau&where=Zurich+%28Region%29


Summary

Hi Tommy. Hier ist das Teilchen. Nachfolgend findest Du Dies und Das zur Nutzung.
Das Ding ist really quick and dirty gemacht und hat sicherlich Potential nach Oben:-)

FAQ
Q: Fängt der WebDataReader nicht existierende Sites oder Nicht- Erreichbarkeit ab?
A: Nein.

Q: Wo werden Abfrageparameter aktualisiert?
A: In der Class org.ms.core.reader.HttpReader. Ausgelagerte Konfigurationen sind derzeit nicht vorgesehen.
=======
WebDataReader for several things
>>>>>>> master@WebDataReader/master
