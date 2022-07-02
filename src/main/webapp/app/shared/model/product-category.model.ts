import { IProduct } from 'app/shared/model/product.model';

export interface IProductCategory {
  id?: number;
  categoryDescription?: string | null;
  ids?: IProduct[] | null;
}

export const defaultValue: Readonly<IProductCategory> = {};
