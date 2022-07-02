import dayjs from 'dayjs';
import { IUsers } from 'app/shared/model/users.model';
import { IAuction } from 'app/shared/model/auction.model';

export interface IBid {
  id?: number;
  bidTime?: string | null;
  amount?: number | null;
  ccy?: string | null;
  userId?: IUsers | null;
  auctionId?: IAuction | null;
}

export const defaultValue: Readonly<IBid> = {};
