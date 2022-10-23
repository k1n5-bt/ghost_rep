from sqlalchemy.orm import declarative_base
from sqlalchemy import Column, Integer, String, Date

Base = declarative_base()


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
    action = Column(String)
    data_id = Column(Integer)
    date = Column(Date)

    def __repr__(self):
        return "<ActionStatistic(action='%s', data_id='%s', date='%s')>" % (
            self.action,
            self.data_id,
            self.date
        )