import dayjs from 'dayjs';
import { IAuction } from 'app/shared/model/auction.model';
import { IProductCategory } from 'app/shared/model/product-category.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IProduct {
  id?: number;
  productname?: string | null;
  productSpecification?: string | null;
  actualCost?: number | null;
  ccy?: string | null;
  creationDate?: string | null;
  ids?: IAuction[] | null;
  productCategoryId?: IProductCategory | null;
  locationId?: ILocation | null;
}

export const defaultValue: Readonly<IProduct> = {};
