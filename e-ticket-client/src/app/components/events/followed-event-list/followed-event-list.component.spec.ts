import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FollowedEventListComponent } from './followed-event-list.component';

describe('FollowedEventListComponent', () => {
  let component: FollowedEventListComponent;
  let fixture: ComponentFixture<FollowedEventListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FollowedEventListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FollowedEventListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
