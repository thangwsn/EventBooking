import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingListUserComponent } from './booking-list-user.component';

describe('BookingListUserComponent', () => {
  let component: BookingListUserComponent;
  let fixture: ComponentFixture<BookingListUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BookingListUserComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookingListUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
