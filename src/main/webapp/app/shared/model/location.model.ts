import { IProduct } from 'app/shared/model/product.model';

export interface ILocation {
  id?: number;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  country?: string | null;
  ids?: IProduct[] | null;
}

export const defaultValue: Readonly<ILocation> = {};
