import { IBid } from 'app/shared/model/bid.model';

export interface IUsers {
  id?: number;
  username?: string | null;
  name?: string | null;
  surname?: string | null;
  tel?: string | null;
  email?: string | null;
  ssn?: string | null;
  status?: string | null;
  id?: IBid | null;
}

export const defaultValue: Readonly<IUsers> = {};
