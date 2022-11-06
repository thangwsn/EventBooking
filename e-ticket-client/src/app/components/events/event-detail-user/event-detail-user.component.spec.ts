import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventDetailUserComponent } from './event-detail-user.component';

describe('EventDetailUserComponent', () => {
  let component: EventDetailUserComponent;
  let fixture: ComponentFixture<EventDetailUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EventDetailUserComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventDetailUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
