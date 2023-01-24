export class Constants {

    static EVENT_STATUS_CREATED = 'CREATED';
    static EVENT_STATUS_OPEN = 'OPENED';
    static EVENT_STATUS_CLOSE = 'CLOSED';
    static EVENT_STATUS_SOLD= 'SOLD';
    static EVENT_STATUS_LIVE = 'LIVE';
    static EVENT_STATUS_FINISH = 'FINISHED';

    static EVENT_TYPE_FREE = 'FREE'
    static EVENT_TYPE_CHARGE = 'CHARGE'

    static BOOKING_STATUS_CREATED = 'CREATED';
    static BOOKING_STATUS_PENDING = 'PENDING';
    static BOOKING_STATUS_COMPLETED = 'COMPLETED';
    static BOOKING_STATUS_CANCEL = 'CANCEL';
    static BOOKING_STATUS_EXPIRED = 'EXPIRED';
    static BOOKING_STATUS_REJECT = 'REJECT';

    static PAYMENT_TYPE_PAYPAL = 'PayPal'
    static PAYMENT_TYPE_STRIPE = '';

    static FOLLOW_CLASS = "bi bi-heart-fill text-danger";
    static UN_FOLLOW_CLASS = "bi bi-heart text-danger";
}