import product from 'app/entities/product/product.reducer';
import productCategory from 'app/entities/product-category/product-category.reducer';
import location from 'app/entities/location/location.reducer';
import auction from 'app/entities/auction/auction.reducer';
import bid from 'app/entities/bid/bid.reducer';
import users from 'app/entities/users/users.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  product,
  productCategory,
  location,
  auction,
  bid,
  users,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
