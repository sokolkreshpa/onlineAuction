import dayjs from 'dayjs';
import { IBid } from 'app/shared/model/bid.model';
import { IProduct } from 'app/shared/model/product.model';

export interface IAuction {
  id?: number;
  bidStartTime?: string | null;
  bidEndTime?: string | null;
  amount?: number | null;
  ccy?: string | null;
  ids?: IBid[] | null;
  productId?: IProduct | null;
}

export const defaultValue: Readonly<IAuction> = {};
