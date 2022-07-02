import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Product from './product';
import ProductCategory from './product-category';
import Location from './location';
import Auction from './auction';
import Bid from './bid';
import Users from './users';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}product`} component={Product} />
        <ErrorBoundaryRoute path={`${match.url}product-category`} component={ProductCategory} />
        <ErrorBoundaryRoute path={`${match.url}location`} component={Location} />
        <ErrorBoundaryRoute path={`${match.url}auction`} component={Auction} />
        <ErrorBoundaryRoute path={`${match.url}bid`} component={Bid} />
        <ErrorBoundaryRoute path={`${match.url}users`} component={Users} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
