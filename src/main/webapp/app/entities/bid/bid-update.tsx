import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUsers } from 'app/shared/model/users.model';
import { getEntities as getUsers } from 'app/entities/users/users.reducer';
import { IAuction } from 'app/shared/model/auction.model';
import { getEntities as getAuctions } from 'app/entities/auction/auction.reducer';
import { IBid } from 'app/shared/model/bid.model';
import { getEntity, updateEntity, createEntity, reset } from './bid.reducer';

export const BidUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.users.entities);
  const auctions = useAppSelector(state => state.auction.entities);
  const bidEntity = useAppSelector(state => state.bid.entity);
  const loading = useAppSelector(state => state.bid.loading);
  const updating = useAppSelector(state => state.bid.updating);
  const updateSuccess = useAppSelector(state => state.bid.updateSuccess);
  const handleClose = () => {
    props.history.push('/bid');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getAuctions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.bidTime = convertDateTimeToServer(values.bidTime);

    const entity = {
      ...bidEntity,
      ...values,
      userId: users.find(it => it.id.toString() === values.userId.toString()),
      auctionId: auctions.find(it => it.id.toString() === values.auctionId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          bidTime: displayDefaultDateTime(),
        }
      : {
          ...bidEntity,
          bidTime: convertDateTimeFromServer(bidEntity.bidTime),
          userId: bidEntity?.userId?.id,
          auctionId: bidEntity?.auctionId?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="onlineAuctionApp.bid.home.createOrEditLabel" data-cy="BidCreateUpdateHeading">
            <Translate contentKey="onlineAuctionApp.bid.home.createOrEditLabel">Create or edit a Bid</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="bid-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('onlineAuctionApp.bid.bidTime')}
                id="bid-bidTime"
                name="bidTime"
                data-cy="bidTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('onlineAuctionApp.bid.amount')} id="bid-amount" name="amount" data-cy="amount" type="text" />
              <ValidatedField label={translate('onlineAuctionApp.bid.ccy')} id="bid-ccy" name="ccy" data-cy="ccy" type="text" />
              <ValidatedField id="bid-userId" name="userId" data-cy="userId" label={translate('onlineAuctionApp.bid.userId')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="bid-auctionId"
                name="auctionId"
                data-cy="auctionId"
                label={translate('onlineAuctionApp.bid.auctionId')}
                type="select"
              >
                <option value="" key="0" />
                {auctions
                  ? auctions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bid" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BidUpdate;
