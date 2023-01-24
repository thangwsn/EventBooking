import { OrganizerGet } from "./organizer.model";

export class EventGet {
   constructor(
      public id: number,
      public title: string,
      public typeString: string,
      public summary: string,
      public listTag: string[],
      public statusString: string,
      public duration: number,
      public sales: number,
      public totalSlot: number,
      public soldSlot: number,
      public remainSlot: number,
      public followerNum: number,
      public organizer: OrganizerGet,
      public locationString: string,
      public imagePathList: string[]
   ) { }

}

export class LocationDTO {
   constructor(
      public street: string,
      public ward: string,
      public district: string,
      public city: string
   ) { }
}

export class EventCreateRequest {
   constructor(
      public title: string,
      public typeString: string,
      public summary: string,
      public description: string,
      public listTag: string,
      public startTimeMs: number,
      public launchTimeMs: number,
      public closeTimeMs: number,
      public duration: number,
      public totalSlot: number,
      public organizerId: number,
      public locationDto: LocationDTO,
      public videoLink: string
   ) { }

}

export class TicketCatalogGetResponse {
   constructor(
      public id: number,
      public title: string,
      public slot: number,
      public soldSlot: number,
      public remainSlot: number,
      public price: number
   ) { }
}

export class EventDetail {
   constructor(
      public id: number,
      public title: string,
      public typeString: string,
      public summary: string,
      public description: string,
      public videoLink: string,
      public listTag: string[],
      public statusString: string,
      public startTime: any,
      public launchTime: any,
      public closeTime: any,
      public duration: number,
      public totalSlot: number,
      public sales: number,
      public soldSlot: number,
      public remainSlot: number,
      public followerNum: number,
      public organizer: any,
      public location: LocationDTO,
      public imagePathList: string[],
      public ticketCatalogList: TicketCatalogGetResponse[],
      public followed: boolean,
      public disableBooking: boolean
   ) { }
}

export class EventEditRequest {
   constructor(
      public title: string,
      public summary: string,
      public description: string,
      public listTag: string,
      public startTimeMs: number,
      public launchTimeMs: number,
      public closeTimeMs: number,
      public duration: number,
      public organizerId: number,
      public locationDto: LocationDTO,
      public videoLink: string
   ) { }
}

export class EventSearchRequest {
      constructor(
         public pageNo: number,
         public pageSize: number,
         public sortField: string,
         public sortDirection: string,
         public type: string,
         public status: string,
         public searchKey: string,
         public organizerId: number
      ) {}
}
