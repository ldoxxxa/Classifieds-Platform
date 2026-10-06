import csv
import shutil
from pathlib import Path

CSV_DATEI = Path("uebersetzungen.csv")
ZIELORDNER = Path("src/main/resources")

with CSV_DATEI.open(encoding="utf-8", newline="") as csv_datei:
    reader = csv.reader(csv_datei, delimiter=";")

    kopfzeile = next(reader)
    sprachen = kopfzeile[1:]

    eintraege = {sprache: [] for sprache in sprachen}

    for zeile in reader:
        if not zeile or zeile[0].strip().startswith("#"):
            continue

        propertyname = zeile[0].strip()

        for index, sprache in enumerate(sprachen):
            wert = zeile[index + 1].strip()
            eintraege[sprache].append(f"{propertyname}={wert}")

ZIELORDNER.mkdir(parents=True, exist_ok=True)

for sprache, zeilen in eintraege.items():
    ziel_datei = ZIELORDNER / f"messages_{sprache}.properties"

    with ziel_datei.open("w", encoding="utf-8") as datei:
        datei.write("\n".join(zeilen))
        datei.write("\n")

erste_sprache = sprachen[0]
shutil.copyfile(
    ZIELORDNER / f"messages_{erste_sprache}.properties",
    ZIELORDNER / "messages.properties"
)