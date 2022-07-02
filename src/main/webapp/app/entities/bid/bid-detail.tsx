import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bid.reducer';

export const BidDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const bidEntity = useAppSelector(state => state.bid.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bidDetailsHeading">
          <Translate contentKey="onlineAuctionApp.bid.detail.title">Bid</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bidEntity.id}</dd>
          <dt>
            <span id="bidTime">
              <Translate contentKey="onlineAuctionApp.bid.bidTime">Bid Time</Translate>
            </span>
          </dt>
          <dd>{bidEntity.bidTime ? <TextFormat value={bidEntity.bidTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="onlineAuctionApp.bid.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{bidEntity.amount}</dd>
          <dt>
            <span id="ccy">
              <Translate contentKey="onlineAuctionApp.bid.ccy">Ccy</Translate>
            </span>
          </dt>
          <dd>{bidEntity.ccy}</dd>
          <dt>
            <Translate contentKey="onlineAuctionApp.bid.userId">User Id</Translate>
          </dt>
          <dd>{bidEntity.userId ? bidEntity.userId.id : ''}</dd>
          <dt>
            <Translate contentKey="onlineAuctionApp.bid.auctionId">Auction Id</Translate>
          </dt>
          <dd>{bidEntity.auctionId ? bidEntity.auctionId.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/bid" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bid/${bidEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BidDetail;
