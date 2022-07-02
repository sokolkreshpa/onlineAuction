import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBid } from 'app/shared/model/bid.model';
import { getEntities as getBids } from 'app/entities/bid/bid.reducer';
import { IUsers } from 'app/shared/model/users.model';
import { getEntity, updateEntity, createEntity, reset } from './users.reducer';

export const UsersUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const bids = useAppSelector(state => state.bid.entities);
  const usersEntity = useAppSelector(state => state.users.entity);
  const loading = useAppSelector(state => state.users.loading);
  const updating = useAppSelector(state => state.users.updating);
  const updateSuccess = useAppSelector(state => state.users.updateSuccess);
  const handleClose = () => {
    props.history.push('/users' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getBids({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...usersEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...usersEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="onlineAuctionApp.users.home.createOrEditLabel" data-cy="UsersCreateUpdateHeading">
            <Translate contentKey="onlineAuctionApp.users.home.createOrEditLabel">Create or edit a Users</Translate>
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
                  id="users-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('onlineAuctionApp.users.username')}
                id="users-username"
                name="username"
                data-cy="username"
                type="text"
              />
              <ValidatedField label={translate('onlineAuctionApp.users.name')} id="users-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('onlineAuctionApp.users.surname')}
                id="users-surname"
                name="surname"
                data-cy="surname"
                type="text"
              />
              <ValidatedField label={translate('onlineAuctionApp.users.tel')} id="users-tel" name="tel" data-cy="tel" type="text" />
              <ValidatedField label={translate('onlineAuctionApp.users.email')} id="users-email" name="email" data-cy="email" type="text" />
              <ValidatedField label={translate('onlineAuctionApp.users.ssn')} id="users-ssn" name="ssn" data-cy="ssn" type="text" />
              <ValidatedField
                label={translate('onlineAuctionApp.users.status')}
                id="users-status"
                name="status"
                data-cy="status"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/users" replace color="info">
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

export default UsersUpdate;
