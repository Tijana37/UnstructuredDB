from neomodel import config, StructuredNode, StructuredRel, UniqueIdProperty, IntegerProperty, StringProperty, \
    FloatProperty, BooleanProperty, DateTimeProperty, ZeroOrOne, ZeroOrMore, One, OneOrMore, RelationshipTo
from enum import Enum


# class Status(Enum):
#     released = "RELEASED"
#     rumored = "RUMORED"

class PersonMovieRel(StructuredRel):
    cast_id = IntegerProperty()
    character_name = StringProperty()


class DepartmentPersonRel(StructuredRel):
    #job_id = IntegerProperty()
    job_name = StringProperty()


class UserMovieRating(StructuredRel):
    rating = FloatProperty()
    timestamp = IntegerProperty()


class Movie(StructuredNode):
    uid = UniqueIdProperty()
    title = StringProperty()
    original_title = StringProperty()
    budget = FloatProperty()
    adult = BooleanProperty()
    movie_id = IntegerProperty()
    imdb_id = StringProperty()
    overview = StringProperty()
    popularity = FloatProperty()
    poster_path = StringProperty()
    release_date = DateTimeProperty()
    revenue = FloatProperty()
    runtime = IntegerProperty()
    status = StringProperty()
    tagline = StringProperty()
    vote_count = IntegerProperty()
    vote_average = FloatProperty()
    # relationships
    belongs_to = RelationshipTo('Collection', 'BELONGS_TO', cardinality=ZeroOrMore)
    has_genre = RelationshipTo('Genre', 'HAS_GENRE', cardinality=ZeroOrMore)
    original_language = RelationshipTo('Language', 'ORIGINAL_LANGUAGE', cardinality=One)
    production_company = RelationshipTo('Company', 'PRODUCTION_BY', cardinality=ZeroOrMore)
    production_country = RelationshipTo('Country', 'PRODUCTION_IN', cardinality=ZeroOrMore)
    spoken_language = RelationshipTo('Language', 'SPOKEN_LANGUAGE', cardinality=OneOrMore)
    has_keyword = RelationshipTo('Keyword', 'HAS_KEYWORD', cardinality=ZeroOrMore)


class Genre(StructuredNode):
    uid = UniqueIdProperty()
    genre_id = IntegerProperty()
    name = StringProperty()


class Collection(StructuredNode):
    uid = UniqueIdProperty()
    collection_id = IntegerProperty()
    name = StringProperty()


class Language(StructuredNode):
    uid = UniqueIdProperty()
    language = StringProperty()
    iso_lang_code = StringProperty()


class Company(StructuredNode):
    uid = UniqueIdProperty()
    name = StringProperty()
    company_id = IntegerProperty()


class Country(StructuredNode):
    uid = UniqueIdProperty()
    iso_country_code = StringProperty()
    name = StringProperty()


class Keyword(StructuredNode):
    uid = UniqueIdProperty()
    keyword_id = IntegerProperty()
    name = StringProperty()


class User(StructuredNode):
    uid = UniqueIdProperty()
    user_id = IntegerProperty()
    rated_movie = RelationshipTo('Movie', 'RATED', model=UserMovieRating, cardinality=OneOrMore)


class Person(StructuredNode):
    uid = UniqueIdProperty()
    person_id = IntegerProperty()
    name = StringProperty()
    gender = IntegerProperty()
    cast_in = RelationshipTo('Movie', 'CAST_IN', model=PersonMovieRel, cardinality=ZeroOrOne)  # zero for non actors
    works_in = RelationshipTo('Department', 'WORKS_IN', model=DepartmentPersonRel,
                              cardinality=One)  # for actors add Acting Department


class Department(StructuredNode):  # Rel Person -> Department with property Job
    uid = UniqueIdProperty()
    name = StringProperty()
