import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IAuction } from 'app/shared/model/auction.model';
import { getEntity, updateEntity, createEntity, reset } from './auction.reducer';

export const AuctionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const products = useAppSelector(state => state.product.entities);
  const auctionEntity = useAppSelector(state => state.auction.entity);
  const loading = useAppSelector(state => state.auction.loading);
  const updating = useAppSelector(state => state.auction.updating);
  const updateSuccess = useAppSelector(state => state.auction.updateSuccess);
  const handleClose = () => {
    props.history.push('/auction');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.bidStartTime = convertDateTimeToServer(values.bidStartTime);
    values.bidEndTime = convertDateTimeToServer(values.bidEndTime);

    const entity = {
      ...auctionEntity,
      ...values,
      productId: products.find(it => it.id.toString() === values.productId.toString()),
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
          bidStartTime: displayDefaultDateTime(),
          bidEndTime: displayDefaultDateTime(),
        }
      : {
          ...auctionEntity,
          bidStartTime: convertDateTimeFromServer(auctionEntity.bidStartTime),
          bidEndTime: convertDateTimeFromServer(auctionEntity.bidEndTime),
          productId: auctionEntity?.productId?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="onlineAuctionApp.auction.home.createOrEditLabel" data-cy="AuctionCreateUpdateHeading">
            <Translate contentKey="onlineAuctionApp.auction.home.createOrEditLabel">Create or edit a Auction</Translate>
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
                  id="auction-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('onlineAuctionApp.auction.bidStartTime')}
                id="auction-bidStartTime"
                name="bidStartTime"
                data-cy="bidStartTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('onlineAuctionApp.auction.bidEndTime')}
                id="auction-bidEndTime"
                name="bidEndTime"
                data-cy="bidEndTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('onlineAuctionApp.auction.amount')}
                id="auction-amount"
                name="amount"
                data-cy="amount"
                type="text"
              />
              <ValidatedField label={translate('onlineAuctionApp.auction.ccy')} id="auction-ccy" name="ccy" data-cy="ccy" type="text" />
              <ValidatedField
                id="auction-productId"
                name="productId"
                data-cy="productId"
                label={translate('onlineAuctionApp.auction.productId')}
                type="select"
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/auction" replace color="info">
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

export default AuctionUpdate;
