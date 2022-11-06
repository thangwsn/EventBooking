import { EventGet } from "./event.model";

export class ItemCreateRequest {
    constructor(
        public ticketCatalogId: number,
        public quantity: number
    ) {}
}

export class BookingCreateRequest {
    constructor(
        public eventId: number,
        public fullName: string,
        public mobile: string,
        public listItem: ItemCreateRequest[],
        public paymentType: string
    ) {}
}

export class ItemCreateResponse {
    constructor(
        public ticketCatalogId: number,
        public quantity: number,
        public listTicketCode: string[]
    ) {}
}

export class BookingGet {
    constructor(
        public id: number,
        public code: string,
        public amount: number,
        public statusString: string,
        public fullName: string,
        public mobile: string,
        public username: string,
        public createTime: any,
        public ticketCatalogList: any
    ) {}
}

export class Ticket {
    constructor(
        public id: number,
        public code: string,
        public price: number,
        public soldTime: number,
        public eventTitle: string,
        public startTime: number,
        public locationString: string,
        public ticketCatalogTitle: string
    ) {}
}

export class Payment {
    constructor(
        public id: number,
        public code: string,
        public typeString: string,
        public createTime: any
    ) {}
}

export class BookingDetail {
    constructor(
        public id: number,
        public code: string,
        public amount: number,
        public statusString: string,
        public fullName: string,
        public mobile: string,
        public userId: number,
        public username: string,
        public createTime: any,
        public ticketCatalogList: any,
        public email: string,
        public payment: Payment,
        public listTicket: Ticket[],
        public event: any,
        public bookingTimeout: any
    ) {}
}



