from typing import List, Dict
from datetime import date

from flask import current_app
from db.models import Statistic, ActionStatistic, Action

from sqlalchemy import func
from sqlalchemy.orm import Session

type_model = {
    'file_desc': Statistic.file_desc,
    'oks': Statistic.oks,
    'okpd': Statistic.okpd,
}


def get_action_counts(start_date: date, end_date: date, _type: str) -> List[Dict]:
    session_factory = current_app.session_factory
    session: Session
    statistic: List
    with session_factory() as session:
        q = session.query(ActionStatistic) \
            .filter(ActionStatistic.date > start_date) \
            .filter(ActionStatistic.date < end_date) \
            .where(ActionStatistic.action_id == Action[_type])
        actions = session.execute(q).scalars().all()
    result: List[Dict] = []
    for item in actions:
        result.append(item.to_dict())
    return result


def get_group_counts(start_date: date, end_date: date, _type: str) -> Dict:
    session_factory = current_app.session_factory
    session: Session
    statistic: List
    model = type_model[_type] if _type in type_model else Statistic.file_desc
    with session_factory() as session:
        statistic = session.query(model, func.count())\
            .filter(Statistic.date > start_date)\
            .filter(Statistic.date < end_date)\
            .group_by(model)\
            .all()
    result: Dict = {}
    for (key, value) in statistic:
        result.update({key: value})
    return result
