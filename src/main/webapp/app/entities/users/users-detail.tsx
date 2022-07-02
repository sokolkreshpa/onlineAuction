import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './users.reducer';

export const UsersDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const usersEntity = useAppSelector(state => state.users.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="usersDetailsHeading">
          <Translate contentKey="onlineAuctionApp.users.detail.title">Users</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{usersEntity.id}</dd>
          <dt>
            <span id="username">
              <Translate contentKey="onlineAuctionApp.users.username">Username</Translate>
            </span>
          </dt>
          <dd>{usersEntity.username}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="onlineAuctionApp.users.name">Name</Translate>
            </span>
          </dt>
          <dd>{usersEntity.name}</dd>
          <dt>
            <span id="surname">
              <Translate contentKey="onlineAuctionApp.users.surname">Surname</Translate>
            </span>
          </dt>
          <dd>{usersEntity.surname}</dd>
          <dt>
            <span id="tel">
              <Translate contentKey="onlineAuctionApp.users.tel">Tel</Translate>
            </span>
          </dt>
          <dd>{usersEntity.tel}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="onlineAuctionApp.users.email">Email</Translate>
            </span>
          </dt>
          <dd>{usersEntity.email}</dd>
          <dt>
            <span id="ssn">
              <Translate contentKey="onlineAuctionApp.users.ssn">Ssn</Translate>
            </span>
          </dt>
          <dd>{usersEntity.ssn}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="onlineAuctionApp.users.status">Status</Translate>
            </span>
          </dt>
          <dd>{usersEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/users" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/users/${usersEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UsersDetail;
