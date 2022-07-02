import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProductCategory } from 'app/shared/model/product-category.model';
import { getEntities as getProductCategories } from 'app/entities/product-category/product-category.reducer';
import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { IProduct } from 'app/shared/model/product.model';
import { getEntity, updateEntity, createEntity, reset } from './product.reducer';

export const ProductUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const productCategories = useAppSelector(state => state.productCategory.entities);
  const locations = useAppSelector(state => state.location.entities);
  const productEntity = useAppSelector(state => state.product.entity);
  const loading = useAppSelector(state => state.product.loading);
  const updating = useAppSelector(state => state.product.updating);
  const updateSuccess = useAppSelector(state => state.product.updateSuccess);
  const handleClose = () => {
    props.history.push('/product');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getProductCategories({}));
    dispatch(getLocations({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.creationDate = convertDateTimeToServer(values.creationDate);

    const entity = {
      ...productEntity,
      ...values,
      productCategoryId: productCategories.find(it => it.id.toString() === values.productCategoryId.toString()),
      locationId: locations.find(it => it.id.toString() === values.locationId.toString()),
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
          creationDate: displayDefaultDateTime(),
        }
      : {
          ...productEntity,
          creationDate: convertDateTimeFromServer(productEntity.creationDate),
          productCategoryId: productEntity?.productCategoryId?.id,
          locationId: productEntity?.locationId?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="onlineAuctionApp.product.home.createOrEditLabel" data-cy="ProductCreateUpdateHeading">
            <Translate contentKey="onlineAuctionApp.product.home.createOrEditLabel">Create or edit a Product</Translate>
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
                  id="product-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('onlineAuctionApp.product.productname')}
                id="product-productname"
                name="productname"
                data-cy="productname"
                type="text"
              />
              <ValidatedField
                label={translate('onlineAuctionApp.product.productSpecification')}
                id="product-productSpecification"
                name="productSpecification"
                data-cy="productSpecification"
                type="text"
              />
              <ValidatedField
                label={translate('onlineAuctionApp.product.actualCost')}
                id="product-actualCost"
                name="actualCost"
                data-cy="actualCost"
                type="text"
              />
              <ValidatedField label={translate('onlineAuctionApp.product.ccy')} id="product-ccy" name="ccy" data-cy="ccy" type="text" />
              <ValidatedField
                label={translate('onlineAuctionApp.product.creationDate')}
                id="product-creationDate"
                name="creationDate"
                data-cy="creationDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="product-productCategoryId"
                name="productCategoryId"
                data-cy="productCategoryId"
                label={translate('onlineAuctionApp.product.productCategoryId')}
                type="select"
              >
                <option value="" key="0" />
                {productCategories
                  ? productCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="product-locationId"
                name="locationId"
                data-cy="locationId"
                label={translate('onlineAuctionApp.product.locationId')}
                type="select"
              >
                <option value="" key="0" />
                {locations
                  ? locations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product" replace color="info">
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

export default ProductUpdate;
