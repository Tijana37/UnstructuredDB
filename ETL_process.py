import pandas as pd
import neomodel
from neomodel import db, config
from ast import literal_eval
from model import Genre, Collection

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

    unique_genres = {item['id']:item['name'] for item in all_genres}

    for genre_key in unique_genres.keys():
        exists = Genre.nodes.get_or_none(genre_id=genre_key)
        if exists is None:
            g = Genre(genre_id=genre_key,name=unique_genres[genre_key])
            g.save()

    return unique_genres

@db.transaction
def etl_collection(df_data):
    collections = df_data['belongs_to_collection']
    unique_collections = dict()

    for c in collections:
        if c is None:
            continue
        mapped = literal_eval(c)
        print(mapped)
        unique_collections[mapped['id']] = mapped['name']

    for collection_key in unique_collections.keys():
        exists = Genre.nodes.get_or_none(collection_id=collection_key)
        if exists is None:
            collection = Collection(collection_id=collection_key,name=unique_collections[collection_key])
            collection.save()

    return unique_collections


@db.transaction
def etl_movies_metadata(df_data):
    df_data.reset_index(inplace=True)
    for index, row in df_data.iterrows():
        if index == 2:
            break

        #adult, belongs_to_collection, budget
        print(row['belongs_to_collection'], row['budget'])


if __name__ == "__main__":
    df = pd.read_csv("movie_database_Relational/movies_metadata.csv")
    df = convert_nan_to_None(df)
    gnr = etl_collection(df)
    print(gnr)


# belongs_to_collection = df["belongs_to_collection"]
# print(belongs_to_collection[:3])
