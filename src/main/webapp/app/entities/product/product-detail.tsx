import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product.reducer';

export const ProductDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const productEntity = useAppSelector(state => state.product.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsHeading">
          <Translate contentKey="onlineAuctionApp.product.detail.title">Product</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productEntity.id}</dd>
          <dt>
            <span id="productname">
              <Translate contentKey="onlineAuctionApp.product.productname">Productname</Translate>
            </span>
          </dt>
          <dd>{productEntity.productname}</dd>
          <dt>
            <span id="productSpecification">
              <Translate contentKey="onlineAuctionApp.product.productSpecification">Product Specification</Translate>
            </span>
          </dt>
          <dd>{productEntity.productSpecification}</dd>
          <dt>
            <span id="actualCost">
              <Translate contentKey="onlineAuctionApp.product.actualCost">Actual Cost</Translate>
            </span>
          </dt>
          <dd>{productEntity.actualCost}</dd>
          <dt>
            <span id="ccy">
              <Translate contentKey="onlineAuctionApp.product.ccy">Ccy</Translate>
            </span>
          </dt>
          <dd>{productEntity.ccy}</dd>
          <dt>
            <span id="creationDate">
              <Translate contentKey="onlineAuctionApp.product.creationDate">Creation Date</Translate>
            </span>
          </dt>
          <dd>
            {productEntity.creationDate ? <TextFormat value={productEntity.creationDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="onlineAuctionApp.product.productCategoryId">Product Category Id</Translate>
          </dt>
          <dd>{productEntity.productCategoryId ? productEntity.productCategoryId.id : ''}</dd>
          <dt>
            <Translate contentKey="onlineAuctionApp.product.locationId">Location Id</Translate>
          </dt>
          <dd>{productEntity.locationId ? productEntity.locationId.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product/${productEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDetail;
