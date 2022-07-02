import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Auction from './auction';
import AuctionDetail from './auction-detail';
import AuctionUpdate from './auction-update';
import AuctionDeleteDialog from './auction-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AuctionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AuctionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AuctionDetail} />
      <ErrorBoundaryRoute path={match.url} component={Auction} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AuctionDeleteDialog} />
  </>
);

export default Routes;
