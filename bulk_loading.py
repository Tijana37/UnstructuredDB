from ast import literal_eval
import pandas as pd


def company(df_data):
    all_companies = df_data['production_companies']
    uniques = dict()
    for row in all_companies:
        if row != [] and row is not None:
            row = literal_eval(row)
            for company in row:
                id, name = company['id'], company['name']
                uniques[id] = name
    pd.DataFrame(data=uniques.values(), index=uniques.keys()).to_csv("unique_companies.csv")

    # set the generated file unique_companies.csv in import folder of the DB
    # load data in neo4j:
    """LOAD CSV WITH HEADERS FROM 'file:///unique_companies.csv' AS row
        MERGE (c:Company{company_id: toInteger(row.id)})
        SET c.name = CASE trim(row.name) WHEN "" THEN null ELSE row.name END"""


def keywords(df_data):
    all_keywords = df_data['keywords']
    uniques = dict()
    for row in all_keywords:
        if row != [] and row is not None:
            row = literal_eval(row)
            for keyword in row:
                id, name = keyword['id'], keyword['name']
                uniques[id] = name

    pd.DataFrame(data=uniques.values(), index=uniques.keys()).to_csv("unique_keywords.csv")

    # load data in neo4j:
    """
    LOAD CSV WITH HEADERS FROM 'file:///unique_keywords.csv' AS row
    MERGE (c:Keyword{keyword_id: toInteger(row.id)})
    SET c.uid = apoc.create.uuid()
    SET c.name = CASE trim(row.keyword) WHEN "" THEN null ELSE row.keyword END
    """


def movies(df_data):
    titles = df_data['title']
    original_titles = df_data['original_title']
    budgets = df_data['budget']
    is_adult = df_data['adult']
    movie_ids = df_data['id']
    imdb_ids = df_data['imdb_id']
    overviews = df_data['overview']
    popularities = df_data['popularity']
    poster_paths = df_data['poster_path']
    release_dates = df_data['release_date']
    revenues = df_data['revenue']
    runtimes = df_data['runtime']
    status = df_data['status']
    taglines = df_data['tagline']
    vote_counts = df_data['vote_count']
    vote_avgs = df_data['vote_average']

    check_lens = list(map(lambda n: len(n),
                          [titles, original_titles, budgets, is_adult, movie_ids,
                           imdb_ids, overviews, popularities,
                           poster_paths, release_dates, revenues,
                           runtimes, status, taglines, vote_counts, vote_avgs]))
    check_lens = set(check_lens)
    assert len(check_lens) == 1

    new_df = pd.DataFrame()
    new_df['titles'] = df_data['title']
    new_df['original_titles'] = df_data['original_title']
    new_df['budgets'] = df_data['budget']
    new_df['is_adult'] = df_data['adult']
    new_df['movie_ids'] = df_data['id']
    new_df['imdb_ids'] = df_data['imdb_id']
    new_df['overviews'] = df_data['overview']
    new_df['popularities'] = df_data['popularity']
    new_df['poster_paths'] = df_data['poster_path']
    new_df['release_dates'] = df_data['release_date']
    new_df['revenues'] = df_data['revenue']
    new_df['runtimes'] = df_data['runtime']
    new_df['status'] = df_data['status']
    new_df['taglines'] = df_data['tagline']
    new_df['vote_counts'] = df_data['vote_count']
    new_df['vote_avgs'] = df_data['vote_average']

    new_df.to_csv("all_movies.csv")

    # load data in neo4j:
    """
    LOAD CSV WITH HEADERS FROM 'file:///all_movies.csv' AS row
    MERGE (c:Movie{movie_id: toInteger(row.movie_ids)})
    WITH row, c, [item in split(row.release_dates, "/") |toInteger(item)] AS dateComponents
    SET c.uid = apoc.create.uuid(),
    c.title = row.titles,
    c.original_title = row.original_titles,
    c.budget = toFloat(row.budgets),
    c.adult = toBoolean(row.is_adult),
    c.imdb_id = toInteger(row.imdb_ids),
    c.overview = row.overviews,
    c.popularity = toFloat(row.popularities),
    c.poster_path = row.poster_paths,
    c.release_date = date({day: dateComponents[1], month: dateComponents[0], year: dateComponents[2]}),
    c.revenue = toFloat(row.revenues),
    c.runtime = toInteger(row.runtimes),
    c.status = row.status,
    c.tagline = row.taglines,
    c.vote_count = toInteger(row.vote_counts),
    c.vote_average = toFloat(row.vote_avgs)
    """


def movies_collection(df_data):
    collections = df_data['belongs_to_collection']
    movies_ids = df_data['id']
    collections_ids = [literal_eval(col)['id'] if col is not None else '' for col in collections]

    # print(collections_ids)
    pd.DataFrame(data=collections_ids, index=movies_ids).to_csv("belongs_to_collection_bulk.csv")

    # load data in neo4j:
    # when None value for movie the Match clause is not satisfied so relationship is not generated
    """
    LOAD CSV WITH HEADERS FROM 'file:///belongs_to_collection_bulk.csv' AS row
    MATCH (m:Movie{movie_id: toInteger(row.movie_id)})
    MATCH (c:Collection{collection_id: toInteger(row.collection_id)})
    CREATE (m)-[r:BELONGS_TO]->(c)
    """


def movies_genres(df_data):
    genres_ids = df_data['genres']
    genres_ids = [
        [dict_genre['id'] for dict_genre in literal_eval(genre_per_movie) if literal_eval(genre_per_movie) is not []]
        for genre_per_movie in genres_ids]
    movies_ids = df_data['id']
    movies_genres_dict = dict(zip(movies_ids, genres_ids))

    assert len(movies_ids) == len(genres_ids)
    movies_pairs = [(key, value) for key, values in movies_genres_dict.items() for value in values]

    pd.DataFrame(data=movies_pairs, columns=['movie_id', 'genre_id']).to_csv("movie_has_genre_bulk.csv")

    # load data in neo4j:
    """
    LOAD CSV WITH HEADERS FROM 'file:///movie_has_genre_bulk.csv' AS row
    MATCH (m:Movie{movie_id: toInteger(row.movie_id)})
    MATCH (c:Genre{genre_id: toInteger(row.genre_id)})
    CREATE (m)-[r:HAS_GENRE]->(c)
    """


def orig_language(df_data):
    original_languages = df_data['original_language']
    movies_ids = df_data['id']

    assert len(movies_ids) == len(original_languages)

    pd.DataFrame(data=original_languages, index=movies_ids).to_csv("movie_orig_lang_bulk.csv")

    """
    LOAD CSV WITH HEADERS FROM 'file:///movie_orig_lang_bulk.csv' AS row
    MATCH (m:Movie{movie_id: toInteger(row.id)})
    MATCH (c:Language{iso_lang_code: row.original_language})
    CREATE (m)-[r:ORIGINAL_LANGUAGE]->(c)
    """


def spoken_language(df_data):
    spoken_languages = df_data['spoken_languages']
    spoken_languages = [[dict_genre['iso_639_1'] for dict_genre in literal_eval(langs_per_movie) if
                         literal_eval(langs_per_movie) is not []] for langs_per_movie in spoken_languages]
    movies_ids = df_data['id']
    assert len(movies_ids) == len(spoken_languages)

    movies_langs_dict = dict(zip(movies_ids, spoken_languages))
    movies_pairs = [(key, value) for key, values in movies_langs_dict.items() for value in values]

    pd.DataFrame(data=movies_pairs, columns=['movie_id', 'lang_iso']).to_csv("movie_spoken_lang_bulk.csv")
    """
    LOAD CSV WITH HEADERS FROM 'file:///movie_spoken_lang_bulk.csv' AS row
    MATCH (m:Movie{movie_id: toInteger(row.movie_id)})
    MATCH (c:Language{iso_lang_code: row.lang_iso})
    CREATE (m)-[r:SPOKEN_LANGUAGE]->(c)
    """


def movie_production_company(df_data):
    production_companies = df_data['production_companies']
    production_companies = [[dict_genre['id'] for dict_genre in literal_eval(company_per_movie) if
                             literal_eval(company_per_movie) is not []] for company_per_movie in production_companies]
    movies_ids = df_data['id']
    assert len(movies_ids) == len(production_companies)

    movies_company_dict = dict(zip(movies_ids, production_companies))
    movies_pairs = [(key, value) for key, values in movies_company_dict.items() for value in values]

    pd.DataFrame(data=movies_pairs, columns=['movie_id', 'company_id']).to_csv("movie_prod_company_bulk.csv")
    """
    LOAD CSV WITH HEADERS FROM 'file:///movie_prod_company_bulk.csv' AS row
    MATCH (m:Movie{movie_id: toInteger(row.movie_id)})
    MATCH (c:Company{company_id: row.company_id})
    CREATE (m)-[r:PRODUCTION_BY]->(c)
    """


def movie_production_country(df_data):
    production_countries = df_data['production_countries']
    production_countries = [[dict_country['iso_3166_1'] for dict_country in literal_eval(country_per_movie) if
                             literal_eval(country_per_movie) is not []] for country_per_movie in production_countries]
    movies_ids = df_data['id']
    assert len(movies_ids) == len(production_countries)

    movies_country_dict = dict(zip(movies_ids, production_countries))
    movies_pairs_country = [(key, value) for key, values in movies_country_dict.items() for value in values]

    pd.DataFrame(data=movies_pairs_country, columns=['movie_id', 'country_iso']).to_csv("movie_prod_country_bulk.csv")

    """
     LOAD CSV WITH HEADERS FROM 'file:///movie_prod_country_bulk.csv' AS row
     MATCH (m:Movie{movie_id: toInteger(row.movie_id)})
     MATCH (c:Country{iso_country_code: row.country_iso})
     CREATE (m)-[r:PRODUCTION_IN]->(c)
     """


def movie_keywords(df_data):
    keywords = df_data['keywords']
    keywords = [[dict_keyword['id'] for dict_keyword in literal_eval(keywords_per_movie) if
                 literal_eval(keywords_per_movie) is not []] for keywords_per_movie in keywords]
    movies_ids = df_data['id']
    assert len(movies_ids) == len(keywords)

    movies_keywords_dict = dict(zip(movies_ids, keywords))
    movies_pairs_keywords = [(key, value) for key, values in movies_keywords_dict.items() for value in values]

    pd.DataFrame(data=movies_pairs_keywords, columns=['movie_id', 'keyword_id']).to_csv("movie_keywords_bulk.csv")

    """
    LOAD CSV WITH HEADERS FROM 'file:///movie_keywords_bulk.csv' AS row
    MATCH (m:Movie{movie_id: toInteger(row.movie_id)})
    MATCH (c:Keyword{keyword_id: toInteger(row.keyword_id)})
    CREATE (m)-[r:HAS_KEYWORD]->(c)
    """


def users_ratings(df_data):
    # data is already formated for reading in DB in the original file
    """
    LOAD CSV WITH HEADERS FROM 'file:///ratings_small.csv' AS row
    MATCH (m:Movie{movie_id: toInteger(row.movieId)})
    MERGE (u:User{user_id: toInteger(row.userId)})
    CREATE (u)-[r:RATED]->(m)
    SET r.rating = toFloat(row.rating),
    r.timestamp = toInteger(row.timestamp)
    """


def cast(df_data):
    cast_rows = df_data['cast']
    movies_ids = df_data['id']

    cast_rows = [
        [{'personId': dict_cast['id'], 'personName': dict_cast['name'], 'personGender': dict_cast['gender'],
          'castId': dict_cast['cast_id'], 'characterName': dict_cast['character']} for
         dict_cast in literal_eval(cast_per_movie) if
         literal_eval(cast_per_movie) is not []] for cast_per_movie in cast_rows]

    assert len(cast_rows) == len(movies_ids)

    cast_dict = dict(zip(movies_ids, cast_rows))
    cast_pairs_movies = [(key, value) for key, values in cast_dict.items() for value in values]

    movies_ids, ids, names, genders, castIds, charNames = [], [], [], [], [], []
    for (key, value) in cast_pairs_movies:
        movies_ids.append(key)
        ids.append(value['personId'])
        names.append(value['personName'])
        genders.append(value['personGender'])
        castIds.append(value['castId'])
        charNames.append(value['characterName'])

    df = pd.DataFrame()
    df['movieId'] = movies_ids
    df['id'] = ids
    df['name'] = names
    df['gender'] = genders
    df['castId'] = castIds
    df['charName'] = charNames
    df.to_csv("cast_bulk.csv")

    """
     LOAD CSV WITH HEADERS FROM 'file:///cast_bulk.csv' AS row
     MATCH (m:Movie{movie_id: toInteger(row.movieId)})
     MERGE (p:Person{person_id: toInteger(row.id)})
     CREATE (p)-[r:CAST_IN]->(m)
     SET r.cast_id = toInteger(row.castId),
     r.character_name = row.charName,
     p.name = row.name,
     p.gender = toInteger(row.gender)
     """


def crew(df_data):
    crew_rows = df_data['crew']
    movies_ids = df_data['id']

    crew_rows = [
        [{'personId': dict_crew['id'], 'personName': dict_crew['name'], 'personGender': dict_crew['gender'],
          'departmentName': dict_crew['department'], 'jobName': dict_crew['job']} for
         dict_crew in literal_eval(crew_per_movie) if
         literal_eval(crew_per_movie) is not []] for crew_per_movie in crew_rows]

    assert len(crew_rows) == len(movies_ids)

    crew_dict = dict(zip(movies_ids, crew_rows))
    crew_pairs_movies = [(key, value) for key, values in crew_dict.items() for value in values]

    movies_ids, ids, names, genders, departments, jobs = [], [], [], [], [], []
    for (key, value) in crew_pairs_movies:
        movies_ids.append(key)
        ids.append(value['personId'])
        names.append(value['personName'])
        genders.append(value['personGender'])
        departments.append(value['departmentName'])
        jobs.append(value['jobName'])

    # data columns
    df = pd.DataFrame()
    df['movieId'] = movies_ids
    df['id'] = ids
    df['name'] = names
    df['gender'] = genders
    df['departmentName'] = departments
    df['jobName'] = jobs
    df.to_csv("crew_bulk.csv")

    """
     LOAD CSV WITH HEADERS FROM 'file:///crew_bulk.csv' AS row
     MATCH (m:Movie{movie_id: toInteger(row.movieId)})
     MATCH (d:Department{name:row.departmentName})
     MERGE (p:Person{person_id: toInteger(row.id)})
     CREATE (p)-[r:CREW_IN]->(m)
     CREATE (p)-[r2:WORKS_IN]-(d)
     SET r2.job_name = row.jobName,
     p.name = row.name,
     p.gender = toInteger(row.gender)
     """
