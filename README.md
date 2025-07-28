# E-sports Turneringsplatform

Study points assignment 1

## Funktionalitet

Når programmet kører, får man en menu med følgende muligheder:

- Tilmeld spiller til turnering (via stored procedure)
- Tilmeld spiller til turnering (via almindelig SQL)
- Registrer hvem der har vundet en kamp
- Se antal sejre for en spiller
- Afslut programmet

## Kom i gang

1. Start PostgreSQL og opret databasen `esports_platform`
2. Kør SQL-filerne i denne rækkefølge:
   - `create_schema.sql`
   - `insert_data.sql`
   - `stored_procedures.sql`
   - `triggers.sql`
3. Åbn projektet i IntelliJ og kør `Main.java`

## Teknologier brugt

- Java (JDK 17)
- PostgreSQL
- IntelliJ IDEA
- JDBC (uden frameworks)

## Projektstruktur

```bash
src/
  └── dk/cphbusiness/Main.ja
