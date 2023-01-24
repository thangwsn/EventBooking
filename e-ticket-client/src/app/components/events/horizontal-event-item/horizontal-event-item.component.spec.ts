import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HorizontalEventItemComponent } from './horizontal-event-item.component';

describe('HorizontalEventItemComponent', () => {
  let component: HorizontalEventItemComponent;
  let fixture: ComponentFixture<HorizontalEventItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HorizontalEventItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HorizontalEventItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
