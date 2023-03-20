import enum
import logging

from sqlalchemy.orm import declarative_base, relationship
from sqlalchemy import Column, Integer, Date, String, TypeDecorator, ForeignKey

Base = declarative_base()


class Action(enum.Enum):
    New = 100
    Edit = 200
    Archive = 300
    Replace = 400


class State(enum.Enum):
    Active = 100
    Canceled = 200
    Replaced = 300


class IntEnum(TypeDecorator):
    """
    Enables passing in a Python enum and storing the enum's *value* in the db.
    The default would have stored the enum's *name* (ie the string).
    """
    impl = Integer
    cache_ok = True

    def __init__(self, enumtype, *args, **kwargs):
        super(IntEnum, self).__init__(*args, **kwargs)
        self._enumtype = enumtype

    def process_bind_param(self, value, dialect):
        if isinstance(value, int):
            return value

        return value.value

    def process_result_value(self, value, dialect):
        return self._enumtype(value)


class Statistic(Base):
    __tablename__ = "show_stat"

    id = Column(Integer, primary_key=True)
    file_desc = Column(String)
    oks = Column(String)
    okpd = Column(String)
    date = Column(Date)

    def __repr__(self):
        return "<Statistic(file_desc='%s', oks='%s', okpd='%s', date-'%s')>" % (
            self.file_desc,
            self.oks,
            self.okpd,
            self.date
        )


class ActionStatistic(Base):
    __tablename__ = "action_stat"

    id = Column(Integer, primary_key=True)
    action_id = Column(IntEnum(Action), default=Action.New)
    data_id = Column(Integer, ForeignKey('data.id'))
    date = Column(Date)

    def __repr__(self):
        return "<ActionStatistic(action_id='%s', data_id='%s', date='%s')>" % (
            self.action_id,
            self.data_id,
            self.date
        )

    def to_dict(self):
        d = {}
        for column in self.__table__.columns:
            d[column.name] = str(getattr(self, column.name))

        return d


class Data(Base):
    __tablename__ = "data"
    id = Column(Integer, primary_key=True)
    file_desc_first_redaction = Column(String)
    file_desc = Column(String)
    state_id = Column(IntEnum(State))
    actions = relationship("ActionStatistic", backref="data")

    def __str__(self):
        result = self.file_desc_first_redaction or self.file_desc
        if len(result) > 17:
            return result[0:17] + '...'
        return result
