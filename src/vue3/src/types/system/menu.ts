export interface Menu {
  id: string;
  name: string;
  sortcode: number;
  menukey: string;
  children: Menu[];
}
