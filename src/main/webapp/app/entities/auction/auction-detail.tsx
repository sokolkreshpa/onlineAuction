import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './auction.reducer';

export const AuctionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const auctionEntity = useAppSelector(state => state.auction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="auctionDetailsHeading">
          <Translate contentKey="onlineAuctionApp.auction.detail.title">Auction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.id}</dd>
          <dt>
            <span id="bidStartTime">
              <Translate contentKey="onlineAuctionApp.auction.bidStartTime">Bid Start Time</Translate>
            </span>
          </dt>
          <dd>
            {auctionEntity.bidStartTime ? <TextFormat value={auctionEntity.bidStartTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="bidEndTime">
              <Translate contentKey="onlineAuctionApp.auction.bidEndTime">Bid End Time</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.bidEndTime ? <TextFormat value={auctionEntity.bidEndTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="onlineAuctionApp.auction.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.amount}</dd>
          <dt>
            <span id="ccy">
              <Translate contentKey="onlineAuctionApp.auction.ccy">Ccy</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.ccy}</dd>
          <dt>
            <Translate contentKey="onlineAuctionApp.auction.productId">Product Id</Translate>
          </dt>
          <dd>{auctionEntity.productId ? auctionEntity.productId.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/auction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/auction/${auctionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AuctionDetail;
