import pandas as pd
import neomodel
from neomodel import db, config
from ast import literal_eval
from model import Genre, Collection, Language, Country, Company, Keyword, Movie

config.DATABASE_URL = f'neo4j://neo4j:db12345@localhost'


def convert_nan_to_None(df) -> pd.DataFrame:
    replaced = df.where(pd.notnull(df), None)
    return replaced


@db.transaction
def etl_genres(df_data):
    genres_rows = df_data['genres']
    print(genres_rows)
    all_genres = []
    for lst in genres_rows:
        all_genres.extend(literal_eval(lst))

    unique_genres = {item['id']: item['name'] for item in all_genres}

    for genre_key in unique_genres.keys():
        exists = Genre.nodes.get_or_none(genre_id=genre_key)
        if exists is None:
            g = Genre(genre_id=genre_key, name=unique_genres[genre_key])
            g.save()

    return unique_genres


@db.transaction
def etl_collection(df_data):
    collections = df_data['belongs_to_collection']
    unique_collections = dict()

    for c in collections:
        if c is None:
            continue
        map_collection = literal_eval(c)
        unique_collections[map_collection['id']] = map_collection['name']

    for collection_key in unique_collections.keys():
        exists = Collection.nodes.get_or_none(collection_id=collection_key)
        if exists is None:
            collection = Collection(collection_id=collection_key, name=unique_collections[collection_key])
            collection.save()

    return unique_collections


def etl_spoken_language(df_data):
    all_rows = df_data['spoken_languages']
    unique_languages = set()
    for row in all_rows:
        row = literal_eval(row)
        for value in row:
            unique_languages.add(value['iso_639_1'])

    unique_languages.remove('xx')
    unique_languages.remove('sh')

    iso_639_languages = pd.read_csv("language-codes_csv.csv", index_col=0)
    for code in unique_languages:
        exists = Language.nodes.get_or_none(iso_lang_code=code)
        if exists is None:
            print("add new lang : ", code)
            lang = Language(iso_lang_code=code, language=iso_639_languages.loc[code][0])
            lang.save()


def etl_languages(df_data):
    all_langs = list(df_data['original_language'].values)
    unique_lans = set(all_langs)
    # remove invalid
    unique_lans.remove(None)
    unique_lans.remove('sh')
    unique_lans.remove('xx')

    unique_lans = list(unique_lans)
    unique_lans.sort()

    iso_639_languages = pd.read_csv("language-codes_csv.csv", index_col=0)

    # check data
    # print(iso_639_languages.loc['ab'][0])
    for code in unique_lans:
        exists = Language.nodes.get_or_none(iso_lang_code=code)
        if exists is None:
            print("add new lang : ", code)
            lang = Language(iso_lang_code=code, language=iso_639_languages.loc[code][0])
            lang.save()


def etl_country(df_data):
    all_countries = df_data['production_countries']
    uniques = dict()
    for row in all_countries:
        if row != [] and row is not None:
            row = literal_eval(row)
            for country in row:
                code, name = country['iso_3166_1'], country['name']
                uniques[code] = name


    for code in uniques:
        exists = Country.nodes.get_or_none(iso_country_code=code)
        if exists is None:
            print("new ocuntry: ", uniques[code])
            Country(iso_country_code=code, name=uniques[code]).save()
        else:
            print("done")


def etl_company(df_data):
    all_companies = df_data['production_companies']
    uniques = dict()
    for row in all_companies:
        if row != [] and row is not None:
            row = literal_eval(row)
            for company in row:
                id, name = company['id'], company['name']
                uniques[id]=name
    for company_id in uniques:
        exists = Company.nodes.get_or_none(company_id=company_id)
        if exists is None:
            print("new company: ", uniques[company_id])
            Company(company_id=company_id, name=uniques[company_id]).save()


def etl_keywords(df_data):
    all_keywords = df_data['keywords']
    uniques = dict()
    for row in all_keywords:
        if row != [] and row is not None:
            row = literal_eval(row)
            for keyword in row:
                id, name = keyword['id'], keyword['name']
                uniques[id] = name

    for keyword_id in uniques:
        exists = Keyword.nodes.get_or_none(keyword_id=keyword_id)
        if exists is None:
            print("new keyword: ", uniques[keyword_id])
            Keyword(keyword_id=keyword_id, name=uniques[keyword_id]).save()


#@db.transaction
def etl_movies_metadata(df_data):
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
                          [titles, original_titles, budgets, is_adult, movie_ids, imdb_ids, overviews, popularities,
                           poster_paths, release_dates, revenues,
                           runtimes, status, taglines, vote_counts, vote_avgs]))
    check_lens = set(check_lens)
    assert len(check_lens) == 1

    for i in range(list(check_lens)[0]):
        exists = Movie.nodes.get_or_none(movie_id=movie_ids[i], imdb_id=imdb_ids[i])
        if exists is None:
            m = Movie(title=titles[i], original_title=original_titles[i], budget=budgets[i], adult=is_adult[i],
                  movie_id=movie_ids[i], imdb_id=imdb_ids[i], overview=overviews[i], popularity=popularities[i],
                  poster_path=poster_paths[i], release_date=release_dates[i], revenue=revenues[i], runtime=runtimes[i],
                  status=status[i], tagline=taglines[i],vote_count=vote_counts[i], vote_average=vote_avgs[i])
            m.save()

def etl_movies_relations(df_data:pd.DataFrame):
    #for  in df_data:
    pass


if __name__ == "__main__":
    df = pd.read_csv("movie_database_Relational/movies_metadata2.csv")
    df = convert_nan_to_None(df)
    # genres = etl_genres(df)
    # collections = etl_collection(df)
    # languages = etl_languages(df)
    # spoken_languages = etl_spoken_language(df)
    countries = etl_country(df)
    #companies = etl_company(df)
    #keywords = etl_keywords(df)

    # df = pd.read_csv("movie_database_Relational/keywords.csv")
    # df = convert_nan_to_None(df)
